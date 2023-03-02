package com.tcs.oci.execution;

import com.tcs.as.model.AccessDevice;
import com.tcs.as.model.AccessDeviceEndpoint;
import com.tcs.oci.commandObj.*;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExecuteUserModifyRequest {
	private static Logger LOGGER = LogManager.getLogger(ExecuteUserModifyRequest.class);

	public void deleteUserModify(HashMap<String, String> paramList1, BaseImpl baseImpl, String userId, String phoneNumber, AccessDeviceEndpoint accessDeviceEndpoint, List<String> SIPAliasList) throws IOException {
		HashMap<String, String> paramList = paramList1;
		LOGGER.info("Executing User Modify NIL command");
		paramList.put("userId", userId);
		//xmlName =  ConfigUtils.ociPath + "UserModifyRequest17sp4Nil.xml";
		//ociCommandObject = new UserModifyRequest17sp4Nil(baseImpl, paramList,xmlName);
		deletePhoneNumber(baseImpl, phoneNumber, paramList);
		deleteEndpoint(baseImpl, accessDeviceEndpoint, paramList);
		deleteSipAlias(baseImpl, SIPAliasList, paramList);
		//response = baseImpl.getResponseDocMap().get("UserModifyRequest17sp4Nil");
		//return response;
	}

	private void deleteSipAlias(BaseImpl baseImpl, List<String> SIPAliasList, HashMap<String, String> paramList) throws IOException {
		BaseOci ociCommandObject;
		String xmlName;
		if (null != SIPAliasList && SIPAliasList.size() != 0) {
			try {
				xmlName = ConfigUtils.ociPath + "UserModifyRequestSipAliasNIL.xml";
				ociCommandObject = new UserModifyRequest17sp4Nil(baseImpl, paramList, xmlName);
				ociCommandObject.execute();
			} catch (OciException e) {
				LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
			}
		}
	}
	private void deleteEndpoint(BaseImpl baseImpl, AccessDeviceEndpoint accessDeviceEndpoint,
			HashMap<String, String> paramList) throws IOException {
		BaseOci ociCommandObject;
		String xmlName;
		if(null!= accessDeviceEndpoint){
			String linePort = accessDeviceEndpoint.getLinePort();
			if(null!=linePort && !(linePort.isEmpty())) {
				try {
					xmlName = ConfigUtils.ociPath + "UserModifyRequestEndPointNIL.xml";
					ociCommandObject = new UserModifyRequest17sp4Nil(baseImpl, paramList, xmlName);
					ociCommandObject.execute();
				} catch (OciException e) {
					LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
				}
			}
		}
	}
	private void deletePhoneNumber(BaseImpl baseImpl, String phoneNumber, HashMap<String, String> paramList) throws IOException{
		BaseOci ociCommandObject;
		String xmlName;
		if (null != phoneNumber && !(phoneNumber.isEmpty())) {
			try {
				xmlName = ConfigUtils.ociPath + "UserModifyRequestPhoneNumberNIL.xml";
				ociCommandObject = new UserModifyRequest17sp4Nil(baseImpl, paramList, xmlName);
				ociCommandObject.execute();
			} catch (OciException e) {
				LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
			}
		}
	}

	public void addUserModify(HashMap<String, String> paramList1, BaseImpl baseImpl, String userId, String phoneNumber, AccessDeviceEndpoint accessDeviceEndpoint, List<String> SIPAliasList) throws IOException {
		HashMap<String, String> paramList = paramList1;
		LOGGER.info("Executing Add User Modify command");
		paramList.put("userId", userId);
		addSipAlias(baseImpl, SIPAliasList, paramList);
		addEndPoint(baseImpl, accessDeviceEndpoint, paramList);
		addPhoneNumber(baseImpl, phoneNumber, paramList);
		//response = baseImpl.getResponseDocMap().get("UserModifyRequest17sp4");
		//return response;
	}

	private void addPhoneNumber(BaseImpl baseImpl, String phoneNumber, HashMap<String, String> paramList) throws IOException {
		BaseOci ociCommandObject;
		String xmlName;
		if (null != phoneNumber && !(phoneNumber.isEmpty())) {
			try {
				paramList.put("phoneNumber", phoneNumber);
				xmlName = ConfigUtils.ociPath + "UserPhoneNumberModifyRequest.xml";
				ociCommandObject = new UserPhoneNumberModifyRequest(baseImpl, paramList, xmlName);
				ociCommandObject.execute();
			} catch (OciException e) {
				LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
			}
		}
	}
	private void addEndPoint(BaseImpl baseImpl, AccessDeviceEndpoint accessDeviceEndpoint,
			HashMap<String, String> paramList) throws IOException{
		BaseOci ociCommandObject;
		String xmlName;
		if(null!= accessDeviceEndpoint){
			String linePort = accessDeviceEndpoint.getLinePort();
			AccessDevice  accessDevice  = accessDeviceEndpoint.getAccessDevice();
			String deviceLevel = accessDevice.getDeviceLevel();
			String deviceName = accessDevice.getDeviceName();
			if(null!=linePort && !(linePort.isEmpty())) {
				try {
					paramList.put("linePort", linePort);
					paramList.put("deviceLevel", deviceLevel); // Maybe not needed
					paramList.put("deviceName", deviceName); //Maybe not needed
					xmlName = ConfigUtils.ociPath + "UserEndPointModifyRequest.xml";
					ociCommandObject = new UserEndPointModifyRequest(baseImpl, paramList, xmlName);
					ociCommandObject.execute();
				} catch (OciException e) {
					LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
				}
			}
		}
	}
	private void addSipAlias(BaseImpl baseImpl, List<String> SIPAliasList, HashMap<String, String> paramList) throws IOException{
		BaseOci ociCommandObject;
		String xmlName;
		if(null!= SIPAliasList && SIPAliasList.size()!=0) {
			try {
				//paramList.put("sipAlias", SIPAliasList.get(0));
				xmlName = ConfigUtils.ociPath + "UserSipAliasModifyRequest.xml";
				ociCommandObject = new UserSipAliasModifyRequest(baseImpl, paramList, SIPAliasList, xmlName);
				ociCommandObject.execute();
			} catch (OciException e) {
				LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
			}
		}
	}
}

