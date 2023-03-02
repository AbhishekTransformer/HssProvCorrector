package com.tcs.as.model;

public class AccessDeviceEndpoint {
	private AccessDevice accessDevice=new AccessDevice();
	private String linePort;
	
	public AccessDevice getAccessDevice() {
		return accessDevice;
	}
	public void setAccessDevice(AccessDevice accessDevice) {
		this.accessDevice = accessDevice;
	}
	public String getLinePort() {
		return linePort;
	}
	public void setLinePort(String linePort) {
		this.linePort = linePort;
	}
}
