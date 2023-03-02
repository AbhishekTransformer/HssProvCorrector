package com.tcs.parser.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class processLog {

	@Id
	private Integer logId;
	private Integer eventId;
	private String description;
	private String daoName;
	private String methodName;
	private String requestUrl;
	private Long timeRequestSend;
	private String request; // Check longBlob'
	private Long timeResponseReceived;
	private String response; // check longblob
	private String processStatus;
	private String processStatusMsg;
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDaoName() {
		return daoName;
	}
	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public Long getTimeRequestSend() {
		return timeRequestSend;
	}
	public void setTimeRequestSend(Long timeRequestSend) {
		this.timeRequestSend = timeRequestSend;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public Long getTimeResponseReceived() {
		return timeResponseReceived;
	}
	public void setTimeResponseReceived(Long timeResponseReceived) {
		this.timeResponseReceived = timeResponseReceived;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getProcessStatusMsg() {
		return processStatusMsg;
	}
	public void setProcessStatusMsg(String processStatusMsg) {
		this.processStatusMsg = processStatusMsg;
	}
	
	
}
