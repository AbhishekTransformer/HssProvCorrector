package com.tcs.corrector.service;

import com.tcs.elastic.service.App;
import com.tcs.generate.utils.FailureReportObj;
import com.tcs.parser.dto.events;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Refiner {
	private static Logger LOGGER = LogManager.getLogger(Refiner.class);
	private static Logger countLogger = LogManager.getLogger("countLogger");
	protected List<events> failEventList;
	protected HashMap<String, Object> indexData = new HashMap<String, Object>();

	public List<events> failEventListRefinement(List<events> failEventsList) {
		failEventList = failEventsList;
		indexData.put("Failed Events", failEventList.size());
		countLogger.info("Failed event list count : " + failEventList.size());
		removeFixerFailures();
		indexData.put("FixerFailedRemoved", failEventList.size());
		countLogger.info("After remove fixer failure , Failed event list count : " + failEventList.size());
		removeDuplicateUsers();
		indexData.put("DuplicatesRemoved", failEventList.size());
		countLogger.info("After remove duplicate users , Failed event list count : " + failEventList.size());
		indexData.put("source", "ScheduledFixer");
		List<FailureReportObj> displayList = new ArrayList<FailureReportObj>();
		try {
			App.app(indexData, displayList);
		} catch (IOException e) {
			LOGGER.error(e);
			e.printStackTrace();
		} catch (ParseException e) {
			LOGGER.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return failEventList;
	}

	private void removeFixerFailures() {
		String fixerId = "";
		if (Environment.PROD.str().equalsIgnoreCase(ConfigUtils.environment)) {
			fixerId = "proFixer";
		} else {
			fixerId = "test_proFixer";
		}

		List<events> toBeRemovedList = new ArrayList<events>();
		for (events ev : failEventList) {
			/*To ignore failures from our own script*/
			if (ev.getRequestUserId().equalsIgnoreCase(fixerId)) {
				LOGGER.info("Fail skipped: Fixer Retry request :" + ev.getEventId());
				toBeRemovedList.add(ev);
			}
		}
		for (events event : toBeRemovedList) {
			if (failEventList.contains(event))
				failEventList.remove(event);
		}
		toBeRemovedList.clear();
	}

	private void removeDuplicateUsers() {
		/*To remove duplicate userIds to prevent multiple fixes for same user*/
		HashMap<String,events> failEventMap=new HashMap<String,events>();
		for(events event:failEventList) {
			if(!failEventMap.containsKey(event.getDocUserId())){
				failEventMap.put(event.getDocUserId(), event);
			}else{
				LOGGER.info("Fail skipped: Duplicate UserId :"+event.getEventId());
			}
		}
		failEventList=new ArrayList<events>(failEventMap.values());
	}
}
