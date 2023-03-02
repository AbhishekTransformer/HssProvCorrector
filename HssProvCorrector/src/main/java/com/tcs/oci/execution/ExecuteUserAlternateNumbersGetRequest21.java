//package com.tcs.oci.execution;
//
//import com.tcs.as.model.AlternateNumber;
//import com.tcs.as.model.AlternateNumberEntry;
//import com.tcs.oci.commandObj.AuthCommand;
//import com.tcs.oci.commandObj.BaseOci;
//import com.tcs.oci.commandObj.LoginCommand;
//import com.tcs.oci.commandObj.UserAlternateNumbersGetRequest21;
//import com.tcs.oci.utils.OciException;
//import com.tcs.oci.utils.TimeInputHandler;
//import com.tcs.parser.utils.ConfigUtils;
//import com.tcs.parser.utils.XmlDocHandlerUpdated;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ExecuteUserAlternateNumbersGetRequest21 extends BaseImpl {
//    private static Logger LOGGER = LogManager.getLogger(ExecuteUserAlternateNumbersGetRequest21.class);
//    
//    public ExecuteUserAlternateNumbersGetRequest21(String clusterId) throws OciException {
//        super(clusterId, "", "");
//    }
//
//    public AlternateNumber runExecution(String userId) {
//        BaseOci ociCommandObject;
//        HashMap<String, String> paramList = new HashMap<>();
//        Document response;
//        paramList.put("timeStamp", TimeInputHandler.getCurrentTimeStamp());
//        String xmlName;
//        try {
//        	System.out.println("Before Clearing Store Index List at begining  :" + ConfigUtils.storeIndexList.toString());
////        	ConfigUtils.clearList();
//        	LOGGER.info("Store Index List at begining :" + ConfigUtils.storeIndexList.toString());
//            LOGGER.info("Assignment :" + userId);
//            LOGGER.info(" start executing Oci Command- ExecuteUserAlternateNumbersGetRequest21 for user : " + userId);
//            LOGGER.info("Executing login command");
//            xmlName = ConfigUtils.ociPath + "login.xml";
//            //xmlName =  "C:\\harshita\\workspace\\HssProvCorrectorConf\\oci\\login.xml";
//            ociCommandObject = new LoginCommand(this, paramList, xmlName);
//            ociCommandObject.execute();
//            LOGGER.info("Executing authorization command");
//            response = this.getResponseDocMap().get("login");
//            System.out.println("Login Response: " + response);
//            paramList.put("nonce", XmlDocHandlerUpdated.getXmlTagValue(response, "nonce", 0));
//            System.out.println(XmlDocHandlerUpdated.getXmlTagValue(response, "nonce", 0));
//            xmlName = ConfigUtils.ociPath + "auth.xml";
//            //xmlName =  "C:\\harshita\\workspace\\HssProvCorrectorConf\\oci\\auth.xml";
//            ociCommandObject = new AuthCommand(this, paramList, xmlName);
//            ociCommandObject.execute();
//
//            LOGGER.info(" command ExecuteUserAlternateNumbersGetRequest21");
//            paramList.put("userId", userId);
//
//            xmlName = ConfigUtils.ociPath + "UserAlternateNumbersGetRequest21.xml";
//            //xmlName = "C:\\harshita\\workspace\\HssProvCorrectorConf\\oci\\UserAlternateNumbersGetRequest21.xml";
//
//            ociCommandObject = new UserAlternateNumbersGetRequest21(this, paramList, xmlName);
//            ociCommandObject.execute();
//            Document errorResponse = this.getResponseDocMap().get("ErrorResponse");
//            response = this.getResponseDocMap().get("UserAlternateNumbersGetRequest21");
//            if (null != response) {
//                LOGGER.info(" command ExecuteUserAlternateNumbersGetRequest21 executed succcessfully");
//                return parseResponse(response);
//            } else {
//                LOGGER.info(" command ExecuteUserAlternateNumbersGetRequest21 error");
//                return new AlternateNumber();  // so that there are default values. Lets hope all necesary checks are there in further steps. NEdd to check
//            }
//        } catch (Exception e) {
//		    LOGGER.error("-----Error in ExecuteUserAlternateNumbersGetRequest21------"+ConfigUtils.getStackTraceString(e));
//		} finally {
//            if (null != this.selfSocket) {
//                try {
//                    this.selfSocket.close();
//                } catch (IOException e) {
//                    LOGGER.error("-----Error in ExecuteUserAlternateNumbersGetRequest21 socket close------" + ConfigUtils.getStackTraceString(e));
//                }
//            }
//        }
//        return new AlternateNumber();
//    }
//
//    private AlternateNumber parseResponse(Document response) {
//        AlternateNumber alternateNumber = new AlternateNumber();
//        LOGGER.info(" Alternate Number Start "+ response);        
//        alternateNumber.setDistinctiveRing(XmlDocHandlerUpdated.getXmlTagValue(response, "distinctiveRing", 0));
//        List<AlternateNumberEntry> alternateEntryList = getAlternateEntriesList(response);
//        
//        alternateNumber.setAlternateEntry(alternateEntryList);
//        LOGGER.info(" Alternate Number"+ alternateNumber);
//        return alternateNumber;
//    }
//
//    private List<AlternateNumberEntry> getAlternateEntriesList(Document response) {
//        List<AlternateNumberEntry> alternateEntryList = new ArrayList<AlternateNumberEntry>();
//        ConfigUtils.clearList();
////        HashMap<Integer, AlternateNumberEntry> alternateEntrytestingMap = new HashMap<>();
//        for (int i = 1; i < 11; i++) {
//            Node node;
//            if (i < 10) {
//                node = response.getElementsByTagName("alternateEntry0" + i).item(0);
//            	LOGGER.info(" Node: in if statement: "+ node);
//        }
//            else
//            {
//                node = response.getElementsByTagName("alternateEntry" + i).item(0);
//            	LOGGER.info(" Node: in else statement: "+ node);
//            }
//
//            if (null != node) {
//                AlternateNumberEntry entry = new AlternateNumberEntry();
//                NodeList childNodes = node.getChildNodes();               
//                if (null != childNodes && childNodes.getLength() > 0) {
//                	LOGGER.info("inside nested if to check the index value:  "+ i);
//                	ConfigUtils.storeIndexList.add(i);
//                    for (int j = 0; j < childNodes.getLength(); j++) {
//                        String nodeName = childNodes.item(j).getNodeName();
//                        String nodeValue = childNodes.item(j).getTextContent();
//                        //System.out.println("Node Name: "+nodeName);
//                        //System.out.println("Node Value: "+nodeValue);
//                        switch (nodeName) {
//                            case "description":
//									entry.setDescription(nodeValue);
//									break;
//							case "phoneNumber":
//									entry.setPhoneNumber(nodeValue);
//									break;
//                            case "ringPattern":
//                                entry.setRingPattern(nodeValue);
//                                break;
//                            case "extension":
//                                entry.setExtension(nodeValue);
//                                break;
//                            default:
//                                System.out.println("What");
//                                break;
//                        }
//                    }
//                }
//                alternateEntryList.add(entry);
//                LOGGER.info("After alternateEntryList.add, value of i:  "+ i);
////                alternateEntrytestingMap.put(i, entry);
//            }
//        }
//        LOGGER.info("alternateENtry list is: "+alternateEntryList.toString());
//        LOGGER.info("Index list testing is: "+ConfigUtils.storeIndexList.toString());
//        for (AlternateNumberEntry entry : alternateEntryList) {
//        	if(entry != null)
//        	{
//        		LOGGER.info(entry);
//        		LOGGER.info(entry.getPhoneNumber());
//        		LOGGER.info(entry.getRingPattern());
//        	}
//        }
//        return alternateEntryList;  //alternateEntrytestingMap
//    }
//    
//    
//	public static void main(String args[]) throws IOException, OciException {
//        AlternateNumber userAlternateResponse = new ExecuteUserAlternateNumbersGetRequest21("khk9dst11.ip.tdk.dk").runExecution("sek2ellen@vk666668.hvoip.dk");
//        //Document userGetResponse = new ExecuteUserGetRequest21sp1("khk23dst2.ip.tdk.dk").runExecution("doc2bob@vk100950.hvoip.dk");
//
//        System.out.println("Distinctive ring " + userAlternateResponse.getDistinctiveRing());
//    }
//}
//

package com.tcs.oci.execution;

import com.tcs.as.model.AlternateNumber;
import com.tcs.as.model.AlternateNumberEntry;
import com.tcs.oci.commandObj.AuthCommand;
import com.tcs.oci.commandObj.BaseOci;
import com.tcs.oci.commandObj.LoginCommand;
import com.tcs.oci.commandObj.UserAlternateNumbersGetRequest21;
import com.tcs.oci.utils.OciException;
import com.tcs.oci.utils.TimeInputHandler;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExecuteUserAlternateNumbersGetRequest21 extends BaseImpl {
    private static Logger LOGGER = LogManager.getLogger(ExecuteUserAlternateNumbersGetRequest21.class);

    public ExecuteUserAlternateNumbersGetRequest21(String clusterId) throws OciException {
        super(clusterId, "", "");
    }

    public AlternateNumber runExecution(String userId) {
        BaseOci ociCommandObject;
        HashMap<String, String> paramList = new HashMap<>();
        Document response;
        paramList.put("timeStamp", TimeInputHandler.getCurrentTimeStamp());
        String xmlName;
        try {
            LOGGER.info("Assignment :" + userId);
            LOGGER.info(" start executing Oci Command- ExecuteUserAlternateNumbersGetRequest21 for user : " + userId);
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

            LOGGER.info(" command ExecuteUserAlternateNumbersGetRequest21");
            paramList.put("userId", userId);

            xmlName = ConfigUtils.ociPath + "UserAlternateNumbersGetRequest21.xml";
            //xmlName = "C:\\harshita\\workspace\\HssProvCorrectorConf\\oci\\UserAlternateNumbersGetRequest21.xml";

            ociCommandObject = new UserAlternateNumbersGetRequest21(this, paramList, xmlName);
            ociCommandObject.execute();
            Document errorResponse = this.getResponseDocMap().get("ErrorResponse");
            response = this.getResponseDocMap().get("UserAlternateNumbersGetRequest21");
            if (null != response) {
                LOGGER.info(" command ExecuteUserAlternateNumbersGetRequest21 executed succcessfully");
                return parseResponse(response);
            } else {
                LOGGER.info(" command ExecuteUserAlternateNumbersGetRequest21 error");
                return new AlternateNumber();  // so that there are default values. Lets hope all necesary checks are there in further steps. NEdd to check
            }
        } catch (Exception e) {
		    LOGGER.error("-----Error in ExecuteUserAlternateNumbersGetRequest21------"+ConfigUtils.getStackTraceString(e));
		} finally {
            if (null != this.selfSocket) {
                try {
                    this.selfSocket.close();
                } catch (IOException e) {
                    LOGGER.error("-----Error in ExecuteUserAlternateNumbersGetRequest21 socket close------" + ConfigUtils.getStackTraceString(e));
                }
            }
        }
        return new AlternateNumber();
    }

    private AlternateNumber parseResponse(Document response) {
        AlternateNumber alternateNumber = new AlternateNumber();
        alternateNumber.setDistinctiveRing(XmlDocHandlerUpdated.getXmlTagValue(response, "distinctiveRing", 0));
        List<AlternateNumberEntry> alternateEntryList = getAlternateEntriesList(response);
        alternateNumber.setAlternateEntry(alternateEntryList);
        return alternateNumber;
    }

    private List<AlternateNumberEntry> getAlternateEntriesList(Document response) {
        List<AlternateNumberEntry> alternateEntryList = new ArrayList<AlternateNumberEntry>();
        for (int i = 1; i < 11; i++) {
            Node node;
            if (i < 10)
                node = response.getElementsByTagName("alternateEntry0" + i).item(0);
            else
                node = response.getElementsByTagName("alternateEntry" + i).item(0);

            if (null != node) {
                AlternateNumberEntry entry = new AlternateNumberEntry();
                NodeList childNodes = node.getChildNodes();
                if (null != childNodes && childNodes.getLength() > 0) {
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        String nodeName = childNodes.item(j).getNodeName();
                        String nodeValue = childNodes.item(j).getTextContent();
                        //System.out.println("Node Name: "+nodeName);
                        //System.out.println("Node Value: "+nodeValue);
                        switch (nodeName) {
                            case "description":
									entry.setDescription(nodeValue);
									break;
							case "phoneNumber":
									entry.setPhoneNumber(nodeValue);
									break;
                            case "ringPattern":
                                entry.setRingPattern(nodeValue);
                                break;
                            case "extension":
                                entry.setExtension(nodeValue);
                                break;
                            default:
                                System.out.println("What");
                                break;
                        }
                    }
                }
                alternateEntryList.add(entry);
            }
        }
        for (AlternateNumberEntry entry : alternateEntryList) {
        	if(entry != null) {
            LOGGER.info(entry.getPhoneNumber());
            LOGGER.info(entry.getRingPattern());
        	}
        }
        return alternateEntryList;
    }
	public static void main(String args[]) throws IOException, OciException {
        AlternateNumber userAlternateResponse = new ExecuteUserAlternateNumbersGetRequest21("khk9dst11.ip.tdk.dk").runExecution("sek2ellen@vk666668.hvoip.dk");
        //Document userGetResponse = new ExecuteUserGetRequest21sp1("khk23dst2.ip.tdk.dk").runExecution("doc2bob@vk100950.hvoip.dk");

        System.out.println("Distinctive ring " + userAlternateResponse.getDistinctiveRing());
    }
}

