package com.tcs.oci.execution;

import com.tcs.as.model.SCAEndpoint;
import com.tcs.as.model.SCAProfile;
import com.tcs.oci.commandObj.AuthCommand;
import com.tcs.oci.commandObj.BaseOci;
import com.tcs.oci.commandObj.LoginCommand;
import com.tcs.oci.commandObj.UserSharedCallAppearanceGetRequest21sp1;
import com.tcs.oci.utils.OciException;
import com.tcs.oci.utils.TimeInputHandler;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteUserSharedCallAppearanceGetRequest21sp1 extends BaseImpl {
	private static Logger LOGGER = LogManager.getLogger(ExecuteUserSharedCallAppearanceGetRequest21sp1.class);

	public ExecuteUserSharedCallAppearanceGetRequest21sp1(String clusterId) {
		super(clusterId, "", "");
	}

	public SCAProfile runExecution(String userId) {

		BaseOci ociCommandObject;
		HashMap<String, String> paramList = new HashMap<>();
		Document response;
		paramList.put("timeStamp", TimeInputHandler.getCurrentTimeStamp());
		String xmlName;
		try {
			LOGGER.info("Assignment :" + userId);
			LOGGER.info(" start executing Oci Command- ExecuteUserSharedCallAppearanceGetRequest21sp1 for user : " + userId);
			LOGGER.info("Executing login command");
			xmlName = ConfigUtils.ociPath + "login.xml";
			//xmlName =  "C:\\harshita\\workspace\\HssProvCorrectorConf\\oci\\login.xml";
			ociCommandObject = new LoginCommand(this, paramList, xmlName);
			ociCommandObject.execute();
			LOGGER.info("Executing authorization command");
			response = this.getResponseDocMap().get("login");
			System.out.println("Login Response: " + response);
			paramList.put("nonce", XmlDocHandlerUpdated.getXmlTagValue(response, "nonce", 0));
			System.out.println(XmlDocHandlerUpdated.getXmlTagValue(response, "nonce", 0));
			xmlName = ConfigUtils.ociPath + "auth.xml";
			//xmlName =  "C:\\harshita\\workspace\\HssProvCorrectorConf\\oci\\auth.xml";
			ociCommandObject = new AuthCommand(this, paramList, xmlName);
			ociCommandObject.execute();

			LOGGER.info(" command ExecuteUserSharedCallAppearanceGetRequest21sp1");
			paramList.put("userId", userId);

			xmlName = ConfigUtils.ociPath + "UserSharedCallAppearanceGetRequest21sp1.xml";
			//xmlName = "C:\\harshita\\workspace\\HssProvCorrectorConf\\oci\\UserSharedCallAppearanceGetRequest21sp1.xml";

			ociCommandObject = new UserSharedCallAppearanceGetRequest21sp1(this, paramList, xmlName);
			ociCommandObject.execute();
			response = this.getResponseDocMap().get("UserSharedCallAppearanceGetRequest21sp1");
			if (null != response) {
				LOGGER.info(" command ExecuteUserSharedCallAppearanceGetRequest21sp1 executed succcessfully");
				return parseResponse(response);
			} else {
				LOGGER.info(" command ExecuteUserSharedCallAppearanceGetRequest21sp1 error");
				return new SCAProfile();  // so that there are default values. Lets hope all necesary checks are there in further steps. NEdd to check
			}
		}catch (Exception e) {
		    LOGGER.error("-----Error in ExecuteUserSharedCallAppearanceGetRequest21sp1------"+ConfigUtils.getStackTraceString(e));
			} finally {
			if (null != this.selfSocket) {
				try {
					this.selfSocket.close();
				} catch (IOException e) {
					LOGGER.error("-----Error in ExecuteUserSharedCallAppearanceGetRequest21sp1 socket close------"+ConfigUtils.getStackTraceString(e));
				}
			}
		}
		return new SCAProfile();
	}

	private SCAProfile parseResponse(Document response) {
		SCAProfile scaProfile = new SCAProfile();
		scaProfile.setEndpointTable(getXmltagList(response));
		return scaProfile;
	}

	public static List<Map<String, String>> getXmltagList(Document doc) {
		NodeList rowList = doc.getElementsByTagName("row");
		List<Map<String, String>> endpointList = new ArrayList<Map<String, String>>();
		if (rowList != null && rowList.getLength() > 0) {
			for (int i = 0; i < rowList.getLength(); i++) {
				NodeList subColList = rowList.item(i).getChildNodes();
				Map<String, String> rowMap = new HashMap<String, String>();

				if (subColList != null && subColList.getLength() > 0) {
					rowMap.put(SCAEndpoint.DEVICE_LEVEL.str(), subColList.item(0).getTextContent());
					rowMap.put(SCAEndpoint.DEVICE_NAME.str(), subColList.item(1).getTextContent());
					rowMap.put(SCAEndpoint.DEVICE_TYPE.str(), subColList.item(2).getTextContent());
					rowMap.put(SCAEndpoint.LINE_PORT.str(), subColList.item(3).getTextContent());
					rowMap.put(SCAEndpoint.SIP_CONTACT.str(), subColList.item(4).getTextContent());
					rowMap.put(SCAEndpoint.PORT_NUMBER.str(), subColList.item(5).getTextContent());
					rowMap.put(SCAEndpoint.DEVICE_SUPPORT_VDM.str(), subColList.item(6).getTextContent());
					rowMap.put(SCAEndpoint.IS_ACTIVE.str(), subColList.item(7).getTextContent());
					rowMap.put(SCAEndpoint.ALLOW_ORIGINATION.str(), subColList.item(8).getTextContent());
					rowMap.put(SCAEndpoint.ALLOW_TERMINATION.str(), subColList.item(9).getTextContent());
					rowMap.put(SCAEndpoint.MAC_ADDRESS.str(), subColList.item(10).getTextContent());
					//System.out.println(SCAEndpoint.DEVICE_LEVEL.str()+":::"+ subColList.item(0).getTextContent());
					//System.out.println(SCAEndpoint.DEVICE_NAME.str()+":::"+ subColList.item(1).getTextContent());

				}
				endpointList.add(rowMap);
			}
		}
		return endpointList;
	}
	public static void main(String args[]) throws IOException, OciException {
		//SCAProfile userSCAResponse = new ExecuteUserSharedCallAppearanceGetRequest21sp1("khk9dst11.ip.tdk.dk").runExecution("sek2ellen@vk666668.hvoip.dk");
		//Document userGetResponse = new ExecuteUserGetRequest21sp1("khk23dst2.ip.tdk.dk").runExecution("doc2bob@vk100950.hvoip.dk");

		ExecuteUserSharedCallAppearanceGetRequest21sp1 oci = new ExecuteUserSharedCallAppearanceGetRequest21sp1("khk9dst11.ip.tdk.dk");
		SCAProfile userSCAResponse = oci.runExecution("sek2ellen@vk666668.hvoip.dk");

		List<Map<String, String>> endPointList = userSCAResponse.getEndpointTable();
		for (Map<String, String> endPoint : endPointList) {
			System.out.println("Start");
			for (Map.Entry<String, String> entry : endPoint.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}
		}
		System.out.println("EndPoint Table " + userSCAResponse.getEndpointTable());
	}
}

