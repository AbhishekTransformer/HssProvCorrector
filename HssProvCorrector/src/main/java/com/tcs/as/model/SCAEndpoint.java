package com.tcs.as.model;

public enum SCAEndpoint {

	DEVICE_LEVEL("Device Level"),
	DEVICE_NAME("Device Name"),
	DEVICE_TYPE("Device Type"),
	LINE_PORT("Line/Port"),
	SIP_CONTACT("SIP Contact"),
	PORT_NUMBER("Port Number"),
	DEVICE_SUPPORT_VDM("Device Support Visual Device Management"),
	IS_ACTIVE("Is Active"),
	ALLOW_ORIGINATION("Allow Origination"),
	ALLOW_TERMINATION("Allow Termination"),
	MAC_ADDRESS("Mac Address");
	
	private String str;
	SCAEndpoint(String str){
		this.str= str;
	}
	
	public String str(){
		return str;
	}
}
