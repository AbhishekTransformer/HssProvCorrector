package com.tcs.oci.execution;

import com.tcs.as.model.AlternateNumberEntry;
import com.tcs.oci.commandObj.BaseOci;
import com.tcs.oci.commandObj.UserAlternateNumbersModifyRequest;
import com.tcs.oci.commandObj.UserAlternateNumbersModifyRequestNIL;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExecuteUserAlternateNumbersModifyRequest {
    //TODO: If mpore than 1 entry. CHnage logic accordingly OR, If reset one, all other will also be re-trigerred
	private static Logger LOGGER = LogManager.getLogger(ExecuteUserAlternateNumbersModifyRequest.class);
    //Date 3 Jan 23 startedList<Integer> to correct alternateNumbr OCi
    List<Integer> indexList = ConfigUtils.getStoreIndexList();
    int indexListSize = indexList.size() - 1;
    public Document deleteAltNum(HashMap<String, String> paramList1, BaseImpl baseImpl, String userId, List<AlternateNumberEntry> alternateNumberList) throws IOException {
        BaseOci ociCommandObject;
        Document response = null;
        String xmlName;
        HashMap<String, String> paramList = paramList1;
        LOGGER.info("Executing ALternate Number delete command");
        try {
            paramList.put("userId", userId);
            xmlName = ConfigUtils.ociPath + "UserAlternateNumbersModifyRequestNIL.xml";
            for(int loopIndex = 0; loopIndex <= indexListSize; loopIndex++)
            {
            	int indexValue = indexList.get(loopIndex);
	            ociCommandObject = new UserAlternateNumbersModifyRequestNIL(baseImpl, paramList, alternateNumberList, xmlName, indexValue, loopIndex);
	            ociCommandObject.execute();
	            response = baseImpl.getResponseDocMap().get("UserAlternateNumbersModifyRequestNIL");
            }
        } catch (OciException e) {
            LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
        }
        return response;
    }

    public Document addAltNum(HashMap<String, String> paramList1, BaseImpl baseImpl, String userId, String distinctiveRing, List<AlternateNumberEntry> alternateNumberList) throws IOException {
        BaseOci ociCommandObject;
        Document response = null;
        String xmlName;
        HashMap<String, String> paramList = paramList1;
        LOGGER.info("Executing ALternate Number Modify command");
        try {
            paramList.put("userId", userId);
            paramList.put("distinctiveRing", distinctiveRing);
            xmlName = ConfigUtils.ociPath + "UserAlternateNumbersModifyRequest.xml";
            for(int loopIndex = indexListSize; loopIndex >= 0; loopIndex -= 1)
            	{
            	int indexValue = indexList.get(loopIndex);
            	LOGGER.info("IndexValue of alternumber while adding "+ indexValue);
            ociCommandObject = new UserAlternateNumbersModifyRequest(baseImpl, paramList, alternateNumberList, xmlName, indexValue, loopIndex);
            ociCommandObject.execute();
            response = baseImpl.getResponseDocMap().get("UserAlternateNumbersModifyRequest");
            }
        } catch (OciException e) {
            LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
        }
        return response;
    }
}
