package com.tcs.as.model;

import java.util.ArrayList;
import java.util.List;

public class AlternateNumber {

	private String distinctiveRing;
	private List<AlternateNumberEntry> alternateEntry=new ArrayList<AlternateNumberEntry>();
	
	public List<AlternateNumberEntry> getAlternateEntry() {
		return alternateEntry;
	}
	public void setAlternateEntry(List<AlternateNumberEntry> alternateEntry) {
		this.alternateEntry = alternateEntry;
	}
	public String getDistinctiveRing() {
		return distinctiveRing;
	}
	public void setDistinctiveRing(String distinctiveRing) {
		this.distinctiveRing = distinctiveRing;
	}
	
}
