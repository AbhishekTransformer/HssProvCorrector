package com.tcs.hss.client;

import com.tcs.as.model.Fault;
import com.tcs.exceptions.Exception1097;
import com.tcs.exceptions.ExceptionAssociationNotDefined;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.HSS_ERROR_CODE;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.stream.XMLStreamException;
import java.util.List;
import java.util.Map;

public class GetHssRequest extends BaseRequest {
	private static Logger LOGGER = LogManager.getLogger(GetHssRequest.class);
	private HssProfile userHssProfile;

	public GetHssRequest(String sessionId, String associationId) {
		super(ConfigUtils.HssGetRequest, Cai3gAction.GET.str());
		try {
			Document doc = this.getRequestDoc();
			doc = XmlDocHandlerUpdated.setTagInXml(doc, "cai3:SessionId", sessionId, 0);
			doc = XmlDocHandlerUpdated.setTagInXml(doc, "associationId", associationId, 0);
			this.setRequestDoc(doc);
		} catch (Exception e) {
			LOGGER.error("Exception in Get Hss " + ConfigUtils.getStackTraceString(e));
		}
	}

	@Override
	public void parseResponse() {  // TODO : make sure to consider the scenario where hss profile does not exist or there is any error in get, as if not consider we may go and fix this user considering that user profile does not have public ids.
		//throw all kind of exceptions as user defined exception so that unexpected issues can be considered
		if (null != this.responseStream) {
			try {
				userHssProfile = new HssProfile();
				Map<String, List<?>> map = null;
				List<String> publicIdList = null;
				List<PrivateProfile> privateIdList = null;
				try {
					map = HSSParserUtils.getPublicIdValues(this.responseStream);
					LOGGER.info("map : " + map);
					publicIdList = (List<String>) map.get("publicIdList");
					privateIdList = (List<PrivateProfile>) map.get("privateIdList");
					LOGGER.info("Public Id Name : " + publicIdList);
					LOGGER.info("Private Id Name : " + privateIdList);
					populateImsi(publicIdList);
				} catch (Exception1097 e) {
					System.out.println("1097 occured");
					Fault fault = new Fault();
					fault.setErrorcode(HSS_ERROR_CODE.ERROR_1097.get());
					fault.setFault(true);
					userHssProfile.setFault(fault);
				} catch (ExceptionAssociationNotDefined e) {
					System.out.println("Association not defined");
					Fault fault = new Fault();
					fault.setErrorcode(HSS_ERROR_CODE.ASSOCIATION_NOT_DEFINED.get());
					fault.setFault(true);
					userHssProfile.setFault(fault);
				}
				userHssProfile.setPublicIdList(publicIdList);
				userHssProfile.setPrivateIdList(privateIdList);
				//parse other parameters and set in this userHssProfile pojo when required
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}

	public HssProfile getUserHssProfile() {
		return userHssProfile;
	}

	public void setUserHssProfile(HssProfile userHssProfile) {
		this.userHssProfile = userHssProfile;
	}

	public void populateImsi(List<String> publicIdList) {
		String temp = "";
		for (String publicId : publicIdList) {
			if (publicId.contains("@")) {
				temp = publicId.split("@")[0];
				if (temp.contains("sip:"))
					temp = temp.replace("sip:", "");
				if (temp.contains("tel:"))
					temp = temp.replace("tel:", "");
				if (isNumeric(temp) && temp.length() >= 15) {
					userHssProfile.setImsi(temp.trim());
					break;
				}
			}
		}
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
