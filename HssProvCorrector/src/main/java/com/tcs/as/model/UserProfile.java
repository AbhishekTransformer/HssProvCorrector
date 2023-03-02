package com.tcs.as.model;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
	private String userId;
	private String phoneNumber;
	private AccessDeviceEndpoint accessDeviceEndpoint=new AccessDeviceEndpoint();
	private List<String> SIPAliasList=new ArrayList<String>();
	private String serviceProviderId;
	private String groupId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public AccessDeviceEndpoint getAccessDeviceEndpoint() {
		return accessDeviceEndpoint;
	}
	public void setAccessDeviceEndpoint(AccessDeviceEndpoint accessDeviceEndpoint) {
		this.accessDeviceEndpoint = accessDeviceEndpoint;
	}
	public List<String> getSIPAliasList() {
		return SIPAliasList;
	}
	public void setSIPAliasList(List<String> sIPAliasList) {
		SIPAliasList = sIPAliasList;
	}
	public String getServiceProviderId() {
		return serviceProviderId;
	}
	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	
}
