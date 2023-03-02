package com.tcs.as.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SCAProfile {
 
	private List<Map<String, String>> endpointTable= new ArrayList<Map<String,String>>();

	public List<Map<String, String>> getEndpointTable() {
		return endpointTable;
	}

	public void setEndpointTable(List<Map<String, String>> endpointTable) {
		this.endpointTable = endpointTable;
	}
	
	
}
