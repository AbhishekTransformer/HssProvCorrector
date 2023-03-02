package com.tcs.parser.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class events {
	
	@Id
	private Integer eventId;
	private String queueName;
	private String asQueueName;
	private Integer asQueueId;
	private String eventStatus;
	private Integer eventStatusId;
	private Long timeEventStatusUpdate;
	private String eventStatusMsg;
	private Long timeReceived;
	private Long timeScheduled;
	private Long timeNextEventRequest;
	private Long timeNextEventResponse;
	private Long timeStartProcess;
	private Long timeEndProcess;
	private String docType;
	private String docUserId;
	private String docServiceUserId;
	private String docGroupId;
	private String docServiceProviderId;
	private String docDomain;
	private String docContent; //check
	private String asUrl;
	private String asClusterId;
	private String requestId;
	private String requestUserId;
	private Integer readerCompId;
	private Integer processorCompId;
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public String getAsQueueName() {
		return asQueueName;
	}
	public void setAsQueueName(String asQueueName) {
		this.asQueueName = asQueueName;
	}
	public Integer getAsQueueId() {
		return asQueueId;
	}
	public void setAsQueueId(Integer asQueueId) {
		this.asQueueId = asQueueId;
	}
	public String getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public Integer getEventStatusId() {
		return eventStatusId;
	}
	public void setEventStatusId(Integer eventStatusId) {
		this.eventStatusId = eventStatusId;
	}
	public Long getTimeEventStatusUpdate() {
		return timeEventStatusUpdate;
	}
	public void setTimeEventStatusUpdate(Long timeEventStatusUpdate) {
		this.timeEventStatusUpdate = timeEventStatusUpdate;
	}
	public String getEventStatusMsg() {
		return eventStatusMsg;
	}
	public void setEventStatusMsg(String eventStatusMsg) {
		this.eventStatusMsg = eventStatusMsg;
	}
	public Long getTimeReceived() {
		return timeReceived;
	}
	public void setTimeReceived(Long timeReceived) {
		this.timeReceived = timeReceived;
	}
	public Long getTimeScheduled() {
		return timeScheduled;
	}
	public void setTimeScheduled(Long timeScheduled) {
		this.timeScheduled = timeScheduled;
	}
	public Long getTimeNextEventRequest() {
		return timeNextEventRequest;
	}
	public void setTimeNextEventRequest(Long timeNextEventRequest) {
		this.timeNextEventRequest = timeNextEventRequest;
	}
	public Long getTimeNextEventResponse() {
		return timeNextEventResponse;
	}
	public void setTimeNextEventResponse(Long timeNextEventResponse) {
		this.timeNextEventResponse = timeNextEventResponse;
	}
	public Long getTimeStartProcess() {
		return timeStartProcess;
	}
	public void setTimeStartProcess(Long timeStartProcess) {
		this.timeStartProcess = timeStartProcess;
	}
	public Long getTimeEndProcess() {
		return timeEndProcess;
	}
	public void setTimeEndProcess(Long timeEndProcess) {
		this.timeEndProcess = timeEndProcess;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getDocUserId() {
		return docUserId;
	}
	public void setDocUserId(String docUserId) {
		this.docUserId = docUserId;
	}
	public String getDocServiceUserId() {
		return docServiceUserId;
	}
	public void setDocServiceUserId(String docServiceUserId) {
		this.docServiceUserId = docServiceUserId;
	}
	public String getDocGroupId() {
		return docGroupId;
	}
	public void setDocGroupId(String docGroupId) {
		this.docGroupId = docGroupId;
	}
	public String getDocServiceProviderId() {
		return docServiceProviderId;
	}
	public void setDocServiceProviderId(String docServiceProviderId) {
		this.docServiceProviderId = docServiceProviderId;
	}
	public String getDocDomain() {
		return docDomain;
	}
	public void setDocDomain(String docDomain) {
		this.docDomain = docDomain;
	}
	public String getDocContent() {
		return docContent;
	}
	public void setDocContent(String docContent) {
		this.docContent = docContent;
	}
	public String getAsUrl() {
		return asUrl;
	}
	public void setAsUrl(String asUrl) {
		this.asUrl = asUrl;
	}
	public String getAsClusterId() {
		return asClusterId;
	}
	public void setAsClusterId(String asClusterId) {
		this.asClusterId = asClusterId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestUserId() {
		return requestUserId;
	}
	public void setRequestUserId(String requestUserId) {
		this.requestUserId = requestUserId;
	}
	public Integer getReaderCompId() {
		return readerCompId;
	}
	public void setReaderCompId(Integer readerCompId) {
		this.readerCompId = readerCompId;
	}
	public Integer getProcessorCompId() {
		return processorCompId;
	}
	public void setProcessorCompId(Integer processorCompId) {
		this.processorCompId = processorCompId;
	}
	
	
	
	
}
