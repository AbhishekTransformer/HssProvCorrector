package com.tcs.parser.utils;

public enum Environment {

	TEST("test"),
	PROD("production");
	
	private String str;

	public String str() {
		return str;
	}
	Environment(String str){
		this.str= str;
	}
	
}
