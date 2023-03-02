package com.tcs.oci.execution;

/*File Name: LoginCommand.class
 Author : m62167
 Company : TCS
 Description : Basic class template from which all scenario execution implementation classes inherit from.       
 */

import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class BaseImpl {
	private static Logger LOGGER = LogManager.getLogger(BaseImpl.class);

	static boolean ALARM_STATE = false;

	protected Socket selfSocket;
	protected String socketAddress;
	protected int socketPort;
	protected String asUserName;
	protected String asPassword;
	protected String notification;
	protected String serviceProvider;
	protected HashMap<String, Document> requestDocMap = new HashMap<>();
	protected HashMap<String, Document> responseDocMap = new HashMap<>();
	protected String startTimeStamp;
	protected ArrayList<String> commandMap;
	protected String clusterId;


	@SuppressWarnings("rawtypes")
	public BaseImpl(final String clusterId, String asUserName2,
					String asPassword2) {
		setParams(clusterId);
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public HashMap<String, Document> getRequestDocMap() {
		return requestDocMap;
	}

	public void setRequestDocMap(HashMap<String, Document> requestDocMap) {
		this.requestDocMap = requestDocMap;
	}

	public HashMap<String, Document> getResponseDocMap() {
		return responseDocMap;
	}

	public void setResponseDocMap(HashMap<String, Document> responseDocMap) {
		this.responseDocMap = responseDocMap;
	}

	public String getAsUserName() {
		return asUserName;
	}

	public void setAsUserName(String asUserName) {
		this.asUserName = asUserName;
	}

	public String getAsPassword() {
		return asPassword;
	}

	public void setAsPassword(String asPassword) {
		this.asPassword = asPassword;
	}

	public Socket getSocket() throws OciException{
		if (this.selfSocket != null  && this.selfSocket.isClosed()) {
			this.selfSocket = createSocket();
		}
		return this.selfSocket;
	}

public void setParams(String clusterId) {

	this.socketAddress = clusterId;
	this.socketPort = 2208;
	this.selfSocket = createSocket();
	if (Environment.PROD.str().equalsIgnoreCase(ConfigUtils.environment)) {
		this.asUserName = "proFixer";
		this.asPassword = "fixer2015";
	} else {
		this.asUserName = ConfigUtils.asTestUserName;
		this.asPassword = ConfigUtils.asTestPassword;
	}
}

	public Socket createSocket(){
		Socket newSocket = null;
		try {
			LOGGER.info("Going to create Socket on - Socket address : " + this.socketAddress + " Socket port : " + this.socketPort);
			newSocket = new Socket(this.socketAddress, this.socketPort);
			LOGGER.info("----Socket Connected------");
		} catch (Exception e) {
			LOGGER.error("----Error in creating socket-----" + ConfigUtils.getStackTraceString(e));
		}

		return newSocket;
	}
}
