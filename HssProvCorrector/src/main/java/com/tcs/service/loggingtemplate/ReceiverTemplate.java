package com.tcs.service.loggingtemplate;

public class ReceiverTemplate {

	public static final String UID_DELIMITER = "::";
	
	public static final String RECEIVER_START = "Starting Receiver ";

	public static final String RECEIVER_END = "Breaking Receiver ";
	
	public static final String RECEIVER_RETRY = "Retrying Receiver";
	
	public static final String JMS_GET_INFO = "Message Received:\n" +
													"From:%s\n" + 
													"UserId:%s\n"+
													"Cluster:%s\n" + 
													"MessageType:%s\n" +
													"Message:%s\n" ; 

}


