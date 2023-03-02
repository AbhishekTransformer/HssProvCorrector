package com.tcs.service.loggingtemplate;

public class LoggingTemplate {
	
	public static final String UID_DELIMITER = "::";
	
	public static final String APP_ID = "TargetApplication:%s\n";	
	
	public static final String WORKER_START = "Starting Worker:%s ";

	public static final String WORKER_END = "Finishing Worker:%s ";
	
	public static final String WORKER_RETRY = "Retrying Worker:%s";
	
	public static final String HTTP_GET_WORKER_INFO ="TargetApplication:%s\n"+ 
													"Method:GET\n" + 
													"URL:%s\n" + 
													"Headers:%s\n" + 
													"Status:%d\n" + 
													"Response:%s\n";

	public static final String HTTP_POST_WORKER_INFO = "TargetApplication:%s\n"+
														"Method:POST\n" + 
														"URL:%s\n" + 
														"Headers:%s\n" + 
														"Body:%s\n" +
														"Status:%d\n" + 
														"Response:%s\n";

	public static final String HTTP_PUT_WORKER_INFO = "TargetApplication:%s\n"+
														"Method:PUT\n" + 
														"URL:%s\n" + 
														"Headers:%s\n" + 
														"Body:%s\n" +
														"Status:%d\n" + 
														"Response:%s\n";

	public static final String HTTP_DELETE_WORKER_INFO = "TargetApplication:%s\n"+
															"Method:DELETE\n" + 
															"URL:%s\n" + 
															"Headers:%s\n" + 
															"Body:%s\n" +
															"Status:%d\n" + 
															"Response:%s\n" ;

	}



