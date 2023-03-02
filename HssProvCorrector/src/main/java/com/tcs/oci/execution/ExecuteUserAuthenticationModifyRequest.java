package com.tcs.oci.execution;

import com.tcs.oci.commandObj.BaseOci;
import com.tcs.oci.commandObj.UserAuthenticationModifyRequest;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.HashMap;

public class ExecuteUserAuthenticationModifyRequest {
    private static Logger LOGGER = LogManager.getLogger(ExecuteUserAuthenticationModifyRequest.class);

    public Document setAuth(HashMap<String, String> paramList, BaseImpl baseImpl, String userId) throws IOException {
        BaseOci ociCommandObject;
        Document response = null;
        String xmlName;
        String password = getAlphaNumericString();
        System.out.println("Password :" + password);
        LOGGER.info("Executing Auth modify command");
        try {
            paramList.put("userId", userId);
            paramList.put("newPassword", password);
            xmlName = ConfigUtils.ociPath + "UserAuthenticationModifyRequest.xml";
            ociCommandObject = new UserAuthenticationModifyRequest(baseImpl, paramList, xmlName);
            ociCommandObject.execute();
            response = baseImpl.getResponseDocMap().get("UserAuthenticationModifyRequest");
        } catch (OciException e) {
            LOGGER.info("OCi Exception: " + ConfigUtils.getStackTraceString(e));
        }
        return response;
    }

    private String getAlphaNumericString() {
        int n = 8;
        String AlphaString = "ABCDEFGHIJKLMNPQRSTUVWXYZ" + "abcdefghijklmnpqrstuvxyz"; //O and o skipped to avoid confusion
        String numericString = "123456789";//0 skipped to avoid confusion
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n / 2; i++) {
            int index = (int) (AlphaString.length() * Math.random());
            sb.append(AlphaString.charAt(index));
            index = (int) (numericString.length() * Math.random());
            sb.append(numericString.charAt(index));
        }
        return sb.toString();
    }
}
