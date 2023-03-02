package com.tcs.hss.client;

import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

public class LogoutRequest extends BaseRequest {
    private static Logger LOGGER = LogManager.getLogger(LogoutRequest.class);

    public LogoutRequest(String sessionId) {
        super(ConfigUtils.HssLogoutRequest, Cai3gAction.LOGOUT.str());
        try {
            Document doc = this.getRequestDoc();
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "cai3:SessionId", sessionId, 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", sessionId, 0);
            this.setRequestDoc(doc);
        } catch (Exception e) {
            LOGGER.error("Exception in Logout " + ConfigUtils.getStackTraceString(e));
        }
    }

    @Override
    public void parseResponse() {
    }
}
