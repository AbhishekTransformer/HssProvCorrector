package com.tcs.elastic.service;

import com.fasterxml.uuid.Generators;
import com.tcs.generate.utils.FailureReportObj;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class App {
	private static Logger GENERAL_LOGGER = LogManager.getLogger("generalLog");
	private static Logger countLogger = LogManager.getLogger("countLogger");

	public static void app(HashMap<String, Object> indexData, List<FailureReportObj> displayList) throws IOException, ParseException {
		if (ConfigUtils.fix) {
			int failedCount = 0;
			int successCount = 0;
			RestHighLevelClient client = Utils.setClient();
			LocalDateTime time = LocalDateTime.now();
			time = time.minusHours(1);  // to be removed in prod
			UUID scriptId = Generators.timeBasedGenerator().generate();
			GENERAL_LOGGER.info("Kibana Insertion : TimeStamp : " + time.toString());
			if (indexData.size() > 0) {
				indexData.put("@timestamp", time.toString());
				for (Map.Entry<String, Object> index : indexData.entrySet()) {
					GENERAL_LOGGER.info("Key: " + index.getKey() + " Value :" + index.getValue());
				}
				setIndex(client, indexData, scriptId.toString());
			}
			if (null != displayList) {
				for (FailureReportObj obj : displayList) {
					HashMap<String, Object> displayIndex = new HashMap<String, Object>();
					displayIndex.put("@timestamp", time.toString());
					displayIndex.put("errorTime", obj.getEvents().getTimeEndProcess().toString());
					displayIndex.put("userId", obj.getEvents().getDocUserId());
					displayIndex.put("groupId", obj.getEvents().getDocGroupId());
					displayIndex.put("serviceProvider", obj.getEvents().getDocServiceProviderId());
					displayIndex.put("failureReason", obj.getFailureReason());
					displayIndex.put("errorMessage", obj.getErrorMessage());
					displayIndex.put("status", obj.getFixStatus());
					displayIndex.put("fixAttempt", obj.getAttemptCount());
					displayIndex.put("source", "ScheduledFixer");
					for (Map.Entry<String, Object> index : displayIndex.entrySet()) {
						GENERAL_LOGGER.info("Key: "+index.getKey()+" Value :"+index.getValue());
					}
					setIndex(client,displayIndex,scriptId.toString());
					if(obj.getFixStatus().contains("failed") || obj.getFixStatus().contains("Failed") || obj.getFixStatus().contains("error"))
						failedCount++;
					else
						successCount++;
				}
		}
		countLogger.info("Failed count: "  + failedCount );
		countLogger.info("Success count: "  + successCount );
		} else {
			GENERAL_LOGGER.info("Fixing off so not establishing connection with Elastic");
		}
    }
	
	private static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date();
        return sdf.format(date);
    }
	
	private static void setIndex(RestHighLevelClient client,HashMap<String,Object>indexData,String scriptId) throws IOException {
		//indexData.put("scriptId",scriptId);
		String indexName;
		//indexName=ConfigUtils.indexName + "-" + getDate();
		indexName=ConfigUtils.indexName;
		IndexRequest indexRequest = new IndexRequest(indexName, "_doc", Generators.timeBasedGenerator().generate().toString()).source(indexData);
		//IndexResponse indexResponse = client.index(indexRequest);
		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
		GENERAL_LOGGER.info("Index Response : "+indexResponse.getResult());
	}
	
	
}
