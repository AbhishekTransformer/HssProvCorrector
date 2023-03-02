package com.tcs.elastic.service;

import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.Environment;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
	public static String id;
	public static long startTime;
	public static long endTime;		
	public static Map<String, String> tempMap = new HashMap<String, String>();
	private static RestHighLevelClient client;	
	public static int elapseTime=180;
	public static RestHighLevelClient getClient() {
		return client;
	}

	public static RestHighLevelClient setClient() {
		if(Environment.PROD.str().equalsIgnoreCase(ConfigUtils.environment)){
		client = new RestHighLevelClient(
                RestClient.builder(
                    new HttpHost("188.181.131.204", 9200, "http"),
                    new HttpHost("188.181.131.203", 9200, "http"),
                    new HttpHost("188.181.131.202", 9200, "http"),
                    new HttpHost("188.181.131.201", 9200, "http")
          ));
		}else{
		client = new RestHighLevelClient(
                RestClient.builder(
            		new HttpHost("188.181.136.22", 9200, "http"),
            		new HttpHost("188.181.136.15", 9200, "http"),
            		new HttpHost("188.181.136.16", 9200, "http")
	        ));
		}
		return client;
	}
	
	public static void closeClient() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
