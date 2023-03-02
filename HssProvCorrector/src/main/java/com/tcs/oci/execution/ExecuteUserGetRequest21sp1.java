package com.tcs.oci.execution;

import com.tcs.as.model.AccessDevice;
import com.tcs.as.model.AccessDeviceEndpoint;
import com.tcs.as.model.UserProfile;
import com.tcs.oci.commandObj.AuthCommand;
import com.tcs.oci.commandObj.BaseOci;
import com.tcs.oci.commandObj.LoginCommand;
import com.tcs.oci.commandObj.UserGetRequest21sp1;
import com.tcs.oci.utils.OciException;
import com.tcs.oci.utils.TimeInputHandler;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ExecuteUserGetRequest21sp1 extends BaseImpl {
    private static Logger LOGGER = LogManager.getLogger(ExecuteUserGetRequest21sp1.class);

    public ExecuteUserGetRequest21sp1(String clusterId) {
        super(clusterId, "", "");
    }

    public UserProfile runExecution(String userId) throws OciException {
        BaseOci ociCommandObject;
        HashMap<String, String> paramList = new HashMap<>();
        Document response;
        paramList.put("timeStamp", TimeInputHandler.getCurrentTimeStamp());
        String xmlName;
        try {
            LOGGER.info("Assignment :" + userId+ " Username: "+ this.asUserName + " password: "+this.asPassword);
            LOGGER.info(" start executing Oci Command- ExecuteUserGetRequest21sp1 for user : " + userId);
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

            LOGGER.info(" command ExecuteUserGetRequest21sp1");
            paramList.put("userId", userId);

            xmlName = ConfigUtils.ociPath + "UserGetRequest21sp1.xml";
            //xmlName = "C:\\harshita\\workspace\\HssProvCorrectorConf\\oci\\UserGetRequest21sp1.xml";

            ociCommandObject = new UserGetRequest21sp1(this, paramList, xmlName);
            ociCommandObject.execute();
            response = this.getResponseDocMap().get("UserGetRequest21sp1");
            if (null != response) {
                LOGGER.info(" command ExecuteUserGetRequest21sp1 executed succcessfully");
                return parseResponse(response, userId);
            } else {
                LOGGER.info(" command ExecuteUserGetRequest21sp1 error");
                Document errorResponse = this.getResponseDocMap().get("ErrorResponse");
                if(errorResponse != null) {
                String errorDescription = XmlDocHandlerUpdated.getXmlTagValue(errorResponse, "summary", 0);
                if(errorDescription != null){
                    if (errorDescription.contains("User not found:") || errorDescription.contains("Unauthorized request:")) {
                        LOGGER.info("User not found");
                        throw new OciException("User Not Found");
                    }
                }
                }
                return null;
            }
        }
		catch(OciException e){
			//-------------------------------------
			LOGGER.info("error message"+e.getMessage());
			if (e.getMessage().contains("User Not Found"))
			{
				throw new OciException("User Not Found");
			}
			else
			{
				throw new OciException("Error while run Execution");
			}
			//--------------------------------------
//			throw new OciException("User Not Found");
		}catch (Exception e) {
		    LOGGER.error("-----Error in ExecuteUserGetRequest21sp1------"+ConfigUtils.getStackTraceString(e));
		    System.out.println("-----Error in ExecuteUserGetRequest21sp1------"+ConfigUtils.getStackTraceString(e));
			} finally {
            if (null != this.selfSocket) {
                try {
                    this.selfSocket.close();
                } catch (IOException e) {
                    LOGGER.error("-----Error in ExecuteUserGetRequest21sp1 socket close------" + ConfigUtils.getStackTraceString(e));
                }
            }
        }
        return null;
    }

    private UserProfile parseResponse(Document response, String userId) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setPhoneNumber(XmlDocHandlerUpdated.getXmlTagValue(response, "phoneNumber", 0));
        userProfile.setSIPAliasList(XmlDocHandlerUpdated.getXmlTagValues(response, "alias"));
        AccessDeviceEndpoint accessDeviceEndpoint = new AccessDeviceEndpoint();
        AccessDevice accessDevice = new AccessDevice();
        accessDevice.setDeviceLevel(XmlDocHandlerUpdated.getXmlTagValue(response, "deviceLevel", 0));
        accessDevice.setDeviceName(XmlDocHandlerUpdated.getXmlTagValue(response, "deviceName", 0));
        accessDeviceEndpoint.setAccessDevice(accessDevice);
        accessDeviceEndpoint.setLinePort(XmlDocHandlerUpdated.getXmlTagValue(response, "linePort", 0));
		userProfile.setAccessDeviceEndpoint(accessDeviceEndpoint);
		/*The following 2 values added for sending this data to kibana when request coming from API.*/
		userProfile.setServiceProviderId(XmlDocHandlerUpdated.getXmlTagValue(response, "serviceProviderId",0));
		userProfile.setGroupId(XmlDocHandlerUpdated.getXmlTagValue(response, "groupId",0));
		return userProfile;
	}
	public static void main(String args[]) throws IOException, OciException {
        UserProfile userGetResponse = new ExecuteUserGetRequest21sp1("khk9dst11.ip.tdk.dk").runExecution("sek2ellen@vk666668.hvoip.dk");
        //UserProfile userGetResponse = new ExecuteUserGetRequest21sp1("alb2dst6.ip.tdk.dk").runExecution("m23655628@vk139179.hvoip.dk");

        System.out.println("Primary line identitiy " + userGetResponse.getAccessDeviceEndpoint().getLinePort());
        System.out.println("DN : " + userGetResponse.getPhoneNumber());
        System.out.println("Sip alias : " + userGetResponse.getSIPAliasList().get(0));
        //System.out.println("Sip alias : "+userGetResponse.getSIPAliasList().get(1));
    }
}

