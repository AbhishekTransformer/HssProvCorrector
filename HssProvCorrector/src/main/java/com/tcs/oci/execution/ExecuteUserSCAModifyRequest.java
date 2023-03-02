package com.tcs.oci.execution;

import com.tcs.as.model.SCAEndpoint;
import com.tcs.oci.commandObj.BaseOci;
import com.tcs.oci.commandObj.UserSharedCallAppearanceAddEndpointRequest14sp2;
import com.tcs.oci.commandObj.UserSharedCallAppearanceDeleteEndpointListRequest14;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExecuteUserSCAModifyRequest {
    private static Logger LOGGER = LogManager.getLogger(ExecuteUserSCAModifyRequest.class);

    public Document deleteSCA(HashMap<String, String> paramList1, BaseImpl baseImpl, String userId, Map<String, String> profileObj) throws IOException {
        BaseOci ociCommandObject;
        Document response = null;
        String xmlName;
        HashMap<String, String> paramList = paramList1;
        LOGGER.info("Executing SCA delete command");
        try {
            paramList.put("userId", userId);
            paramList.put("deviceLevel", profileObj.get(SCAEndpoint.DEVICE_LEVEL.str()));
            paramList.put("deviceName", profileObj.get(SCAEndpoint.DEVICE_NAME.str()));
            paramList.put("linePort", profileObj.get(SCAEndpoint.LINE_PORT.str()));
            xmlName = ConfigUtils.ociPath + "UserSharedCallAppearanceDeleteEndpointListRequest14.xml";
            ociCommandObject = new UserSharedCallAppearanceDeleteEndpointListRequest14(baseImpl, paramList, xmlName);
            ociCommandObject.execute();
            response = baseImpl.getResponseDocMap().get("UserSharedCallAppearanceDeleteEndpointListRequest14");
        } catch (OciException e) {
            LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
        }
        return response;
    }

    public Document addSCA(HashMap<String, String> paramList1, BaseImpl baseImpl, String userId, Map<String, String> profileObj) throws IOException {
        BaseOci ociCommandObject;
        Document response = null;
        String xmlName;
        HashMap<String, String> paramList = paramList1;
        LOGGER.info("Executing SCA Modify command");
        try {
            paramList.put("userId", userId);
            paramList.put("deviceLevel", profileObj.get(SCAEndpoint.DEVICE_LEVEL.str()));
            paramList.put("deviceName", profileObj.get(SCAEndpoint.DEVICE_NAME.str()));
            paramList.put("linePort", profileObj.get(SCAEndpoint.LINE_PORT.str()));
            paramList.put("isActive", profileObj.get(SCAEndpoint.IS_ACTIVE.str()));
            paramList.put("allowOrigination", profileObj.get(SCAEndpoint.ALLOW_ORIGINATION.str()));
            paramList.put("allowTermination", profileObj.get(SCAEndpoint.ALLOW_TERMINATION.str()));
            xmlName = ConfigUtils.ociPath + "UserSharedCallAppearanceAddEndpointRequest14sp2.xml";
            ociCommandObject = new UserSharedCallAppearanceAddEndpointRequest14sp2(baseImpl, paramList, xmlName);
            ociCommandObject.execute();
            response = baseImpl.getResponseDocMap().get("UserSharedCallAppearanceAddEndpointRequest14sp2");
        } catch (OciException e) {
            LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
        }
        return response;
    }
}
