package com.tcs.as.model;

public class Fault {
	private boolean isFault=false;
	private int faultcode;
	private String faultreason;
	private String errorcode;
	private String errormessage;
	public int getFaultcode() {
		return faultcode;
	}
	public void setFaultcode(int faultcode) {
		this.faultcode = faultcode;
	}
	public String getFaultreason() {
		return faultreason;
	}
	public void setFaultreason(String faultreason) {
		this.faultreason = faultreason;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrormessage() {
		return errormessage;
	}
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}
	public boolean isFault() {
		return isFault;
	}
	public void setFault(boolean isFault) {
		this.isFault = isFault;
	}
	
	
}
