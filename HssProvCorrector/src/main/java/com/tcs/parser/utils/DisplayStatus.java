package com.tcs.parser.utils;

public enum DisplayStatus {

	SUCCESS_RETRY("Retried"),
	FAILED_RETRY("Retry Failed"),
	SUCCESS_FIX("Fixed"),
	FAILED_FIX("Fix Failed, Manual Intervention required"),
	SCHEDULED("Scheduled For Fix");
	
	private String str;
	DisplayStatus(String str){
		this.str=str;
	}
	public String str(){
		return str;
	}
}
