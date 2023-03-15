package com.tcs.corrector.service;

import com.tcs.as.model.AccessDeviceEndpoint;
import com.tcs.as.model.AlternateNumberEntry;
import com.tcs.as.model.AsProfile;
import com.tcs.as.model.SCAEndpoint;
import com.tcs.oci.execution.*;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fixer {
    private static Logger LOGGER = LogManager.getLogger(Fixer.class);

    public boolean fixPublicIds(AsProfile asProfile, String clusterId, boolean allScaDevice) {
        boolean isOciSuccess = true;
        String userId = asProfile.getProfile().getUserId();
        String phoneNumber = asProfile.getProfile().getPhoneNumber();
        AccessDeviceEndpoint accessDeviceEndpoint = asProfile.getProfile().getAccessDeviceEndpoint();
        List<String> SIPAliasList = asProfile.getProfile().getSIPAliasList();
        String distinctiveRing = asProfile.getAlternateNumber().getDistinctiveRing();
        List<AlternateNumberEntry> alternateEntry = asProfile.getAlternateNumber().getAlternateEntry();
        LOGGER.info("alternateEntry in fixpublicId: " + alternateEntry);
        LOGGER.info("phoneNumber in fixpublicId: " + phoneNumber);
        LOGGER.info("userId in fixpublicId: " + userId);
        List<Map<String, String>> endpointTable = asProfile.getScaProfile().getEndpointTable();
        LOGGER.info("Fixer:alternateEntry "+alternateEntry);
        LOGGER.info("Fixer:endpointTable "+endpointTable);

        ExecuteUserModifyRequest userModify = new ExecuteUserModifyRequest();
        ExecuteUserAlternateNumbersModifyRequest altNum = new ExecuteUserAlternateNumbersModifyRequest();
        ExecuteUserSCAModifyRequest scaReq = new ExecuteUserSCAModifyRequest();
        ExecuteUserAuthenticationModifyRequest setSipPwd = new ExecuteUserAuthenticationModifyRequest();

        boolean isAltNumExist = (null != alternateEntry && alternateEntry.size() != 0) ? true : false; //Check if even size is non 0, is it possible to have blank values
        boolean isSCAExist = (null != endpointTable && endpointTable.size() != 0) ? true : false;
        boolean isOtherPubIdExist = false;
        ExecuteOCI userFixer = null;
        try {
        	
            userFixer = new ExecuteOCI(clusterId);
            HashMap<String, String> paramList = userFixer.init();
            //removed exception throw till here. As, if one OCI fails and goes to catch, we don't want other OCi to fail.

            List<String> tempSipAliasList = new ArrayList<String>();
            if (null != SIPAliasList && SIPAliasList.size() != 0) {
                for (String sipAlias : SIPAliasList) {
                    tempSipAliasList.add(sipAlias);
                }
                isOtherPubIdExist = true;
                LOGGER.info("True A");
            }
            LOGGER.info("Fixer:paramList "+paramList+" userFixer:"+userFixer+" userId"+userId+" phoneNumber:"+phoneNumber+" tempSipAliasList:"+tempSipAliasList);
            userModify.deleteUserModify(paramList, userFixer, userId, phoneNumber, accessDeviceEndpoint, tempSipAliasList);

            LOGGER.info("Fixer:isAltNumExist "+isAltNumExist);
            if (isAltNumExist) {
                List<AlternateNumberEntry> tempList = new ArrayList<AlternateNumberEntry>();
                ;
                for (AlternateNumberEntry alt : alternateEntry) {
                    tempList.add(alt);
                }
                System.out.println("Indexing List: "+ConfigUtils.storeIndexList.toString());
                LOGGER.info("Fixer:paramList "+endpointTable+" userFixer:"+userFixer+" userId"+userId+" tempList:"+tempList);
                altNum.deleteAltNum(paramList, userFixer, userId, tempList);
            }
            if (isSCAExist) {
                for (Map<String, String> profileObj : endpointTable) {
                    if (profileObj.get(SCAEndpoint.DEVICE_LEVEL.str()).equals("System") && profileObj.get(SCAEndpoint.DEVICE_NAME.str()).equals("Mobile")) {
                        
                    	scaReq.deleteSCA(paramList, userFixer, userId, profileObj);
                        isOtherPubIdExist = true;
                        LOGGER.info("True B");
                    }
                }
            }
            if (allScaDevice) {
                for (Map<String, String> profileObj : endpointTable) {
                    if (!(profileObj.get(SCAEndpoint.DEVICE_LEVEL.str()).equals("System") && profileObj.get(SCAEndpoint.DEVICE_NAME.str()).equals("Mobile"))) {
                        scaReq.deleteSCA(paramList, userFixer, userId, profileObj);
                        isOtherPubIdExist = true;
                        LOGGER.info("True C");
                        Thread.sleep(10);
                    }
                }
                for (Map<String, String> profileObj : endpointTable) {
                    if (!(profileObj.get(SCAEndpoint.DEVICE_LEVEL.str()).equals("System") && profileObj.get(SCAEndpoint.DEVICE_NAME.str()).equals("Mobile"))) {
                        scaReq.addSCA(paramList, userFixer, userId, profileObj);
                        isOtherPubIdExist = true;
                        LOGGER.info("True D");
                        Thread.sleep(10);
                    }
                }
            }

            List<String> tempSipAliasList2 = new ArrayList<String>();
            if (null != SIPAliasList && SIPAliasList.size() != 0) {
                for (String sipAlias : SIPAliasList) {
                    tempSipAliasList2.add(sipAlias);
                }
                isOtherPubIdExist = true;
                LOGGER.info("True E");
            }
            userModify.addUserModify(paramList, userFixer, userId, phoneNumber, accessDeviceEndpoint, tempSipAliasList2);
            
            if (isSCAExist) {
            	//These two loops work for +45 number and it tries to arrange it considering 
            	//+45 at 0th index and others as it was
            	//-------------------------------------------------------------------------------------------
            	
 /*           	
            	
            	ArrayList<Map<String, String>> tmpObj = new ArrayList<>();	
            	for(Map<String, String>tmpProfileObj : endpointTable)
            	{
            		 if (tmpProfileObj.get(SCAEndpoint.DEVICE_LEVEL.str()).equals("System") && tmpProfileObj.get(SCAEndpoint.DEVICE_NAME.str()).equals("Mobile")) {
            			 
            				 String linePort = tmpProfileObj.get(SCAEndpoint.LINE_PORT.str());
            				 LOGGER.info("LinePort value"+ linePort);
            				 if (linePort.contains("+45"))
            				 {
            					 LOGGER.info("tempProfile and tmpObj: "+ tmpProfileObj+ " "+ tmpObj);
            					 tmpObj.add(tmpProfileObj);
            					 LOGGER.info("tmpObj"+ tmpObj);
            					 break;
            				 }
            			 
            		 }
            	}
            	LOGGER.info("OutSIde for tmpObj"+ tmpObj);
            	for(Map<String, String>tmpProfileObj1 : endpointTable)
                 	{
                 		 if (tmpProfileObj1.get(SCAEndpoint.DEVICE_LEVEL.str()).equals("System") && tmpProfileObj1.get(SCAEndpoint.DEVICE_NAME.str()).equals("Mobile")) 
                 		 {
                 			String linePort = tmpProfileObj1.get(SCAEndpoint.LINE_PORT.str());
                 			 if (!linePort.contains("+45"))
            				 {
            					 tmpObj.add(tmpProfileObj1);
            					 break;
            				 }
                 				 
                 		 }
                 	}
            			 
            	LOGGER.info("tmpObj and endpointTable: "+ tmpObj+", "+ endpointTable);
*/            	
                for (Map<String, String> profileObj : endpointTable) {  //previously this (Map<String, String> profileObj : endpointTable)
                    if ((profileObj.get(SCAEndpoint.DEVICE_LEVEL.str()).equals("System") && profileObj.get(SCAEndpoint.DEVICE_NAME.str()).equals("Mobile"))) 
                    {
                        scaReq.addSCA(paramList, userFixer, userId, profileObj);
                        isOtherPubIdExist = true;
                        LOGGER.info("True F");
                    }
                }
        }
            if (isAltNumExist) {
                List<AlternateNumberEntry> tempList = new ArrayList<AlternateNumberEntry>();
                
                for (AlternateNumberEntry alt : alternateEntry) {
                    tempList.add(alt);
                }
                altNum.addAltNum(paramList, userFixer, userId, distinctiveRing, tempList);
            }
            if ((null != phoneNumber && !(phoneNumber.isEmpty())) || (null != accessDeviceEndpoint && null != accessDeviceEndpoint.getLinePort() && !(accessDeviceEndpoint.getLinePort().isEmpty()))) {
                isOtherPubIdExist = true;
                LOGGER.info("True G");
            }
            if (isOtherPubIdExist || isAltNumExist) {
                setSipPwd.setAuth(paramList, userFixer, userId);
                LOGGER.info("Checking " + isOtherPubIdExist + isAltNumExist + isSCAExist + allScaDevice);
            } else {
                LOGGER.info("Since no public id was present on AS, no fix OCIs were trigerred");
                LOGGER.info("Checking " + isOtherPubIdExist + isAltNumExist + isSCAExist + allScaDevice);
                isOciSuccess = false;
            }
        
        }
        catch (Exception e) {
            isOciSuccess = false;
            LOGGER.error("-----Error in OCI init------" + ConfigUtils.getStackTraceString(e));
        } finally {
            try {
                userFixer.disconnect();
            } catch (IOException e) {
                LOGGER.error("-----Error in OCI init------" + ConfigUtils.getStackTraceString(e));
            }
        }
        return isOciSuccess;
    }
}
