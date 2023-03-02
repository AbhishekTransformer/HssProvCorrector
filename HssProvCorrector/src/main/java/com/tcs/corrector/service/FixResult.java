package com.tcs.corrector.service;

public enum FixResult {
	FAILED_MANUAL("Fix Failed, Manual Intervention required"),
	FAILED_INUSE("Fix Failed, Phone Number already in use"),
	FAILED_INCOM_PROV("Fix Failed, Incomplete Provisioning"),
	NOT_ATTEMPTED("Fix not attempted"),
	SUCCESS("Fix Success");
	
	private String str;

	FixResult(String str) {
		this.str = str;
	}

	public String str() {
		return str;
	}

}
