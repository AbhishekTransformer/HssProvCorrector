package com.tcs.oci.execution;

import com.tcs.oci.commandObj.AuthCommand;
import com.tcs.oci.commandObj.BaseOci;
import com.tcs.oci.commandObj.LoginCommand;
import com.tcs.oci.utils.OciException;
import com.tcs.oci.utils.TimeInputHandler;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.HashMap;

public class ExecuteOCI extends BaseImpl {
    private static Logger LOGGER = LogManager.getLogger(ExecuteOCI.class);

    public ExecuteOCI(String clusterId) {
        super(clusterId, "", "");
    }

    public HashMap<String, String> init() throws IOException, OciException {
        HashMap<String, String> paramList = new HashMap<>();
        Document response = executeLogin(paramList);
        executeAuthentication(response, paramList);
        return paramList;
    }

    public void disconnect() throws IOException {
        if (null != this.selfSocket) {
            this.selfSocket.close();
        }
    }

    private void executeAuthentication(Document response, HashMap<String, String> paramList) throws OciException, IOException {
        BaseOci ociCommandObject;
        String xmlName;
        LOGGER.info("Executing authorization command");
        paramList.put("nonce", XmlDocHandlerUpdated.getXmlTagValue(response, "nonce", 0));
        xmlName = ConfigUtils.ociPath + "auth.xml";
        ociCommandObject = new AuthCommand(this, paramList, xmlName);
        ociCommandObject.execute();
    }

    private Document executeLogin(HashMap<String, String> paramList) throws OciException, IOException {
        BaseOci ociCommandObject;
        Document response;
        String xmlName;
        paramList.put("timeStamp", TimeInputHandler.getCurrentTimeStamp());
        LOGGER.info("Executing login command");
        xmlName = ConfigUtils.ociPath + "login.xml";
        ociCommandObject = new LoginCommand(this, paramList, xmlName);
        ociCommandObject.execute();
        response = this.getResponseDocMap().get("login");
        return response;
    }
}

