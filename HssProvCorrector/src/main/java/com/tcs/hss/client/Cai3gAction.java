package com.tcs.hss.client;

public enum Cai3gAction {
 LOGIN("Login"),
 GET("Get"),
 DELETE("Delete"),
 LOGOUT("Logout");
	
	private String str;
	Cai3gAction(String str){
		this.str=str;
	}
	public String str(){
		return str;
	}
}
