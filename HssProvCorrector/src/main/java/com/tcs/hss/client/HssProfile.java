package com.tcs.hss.client;

import com.tcs.as.model.Fault;

import java.util.List;

public class HssProfile {

	private List<String> publicIdList;
	private String profile;
	private Fault fault;
	private String imsi;


	//private String associationId;
	//private String imsi;
	private List<PrivateProfile> privateIdList;

	public List<String> getPublicIdList() {
		return publicIdList;
	}

	public void setPublicIdList(List<String> publicIdList) {
		this.publicIdList = publicIdList;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Fault getFault() {
		return fault;
	}

	public void setFault(Fault fault) {
		this.fault = fault;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public List<PrivateProfile> getPrivateIdList() {
		return privateIdList;
	}

	public void setPrivateIdList(List<PrivateProfile> privateIdList) {
		System.out.println(privateIdList);
		this.privateIdList = privateIdList;
	}
}
