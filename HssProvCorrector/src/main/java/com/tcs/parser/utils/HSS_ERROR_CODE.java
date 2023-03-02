package com.tcs.parser.utils;

public enum HSS_ERROR_CODE {
	ERROR_1097("1097"),
	ASSOCIATION_NOT_DEFINED("13005");
	private String errorCode;
	
	private HSS_ERROR_CODE(String errorCode){
		this.errorCode = errorCode;
	}
	public String get(){
		return errorCode;
	}

}
