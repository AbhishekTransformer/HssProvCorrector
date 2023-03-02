package com.tcs.generate.utils;

import com.tcs.parser.dto.events;

public class FailureReportObj {

	public FailureReportObj() {
		super();
		this.attemptCount = "First";
	}
	private events events;
	private String failureReason;
	private String errorMessage;
	private String attemptCount;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	private String fixStatus;
	public events getEvents() {
		return events;
	}
	public void setEvents(events events) {
		this.events = events;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public String getFixStatus() {
		return fixStatus;
	}
	public void setFixStatus(String fixStatus) {
		this.fixStatus = fixStatus;
	}
	public String getAttemptCount() {
		return attemptCount;
	}
	public void setAttemptCount(String attemptCount) {
		this.attemptCount = attemptCount;
	}	
}
