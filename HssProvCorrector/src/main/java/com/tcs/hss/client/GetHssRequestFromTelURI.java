package com.tcs.hss.client;

import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.stream.XMLStreamException;

public class GetHssRequestFromTelURI extends BaseRequest {
    private static Logger LOGGER = LogManager.getLogger(GetHssRequestFromTelURI.class);
    private String username;

    public GetHssRequestFromTelURI(String sessionId, String telUri) {
        super(ConfigUtils.HssGetRequestForTelURI, Cai3gAction.GET.str());
        try {
            Document doc = this.getRequestDoc();
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "cai3:SessionId", sessionId, 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "hss:impu", telUri, 0);
            this.setRequestDoc(doc);
        } catch (Exception e) {
            LOGGER.error("Exception in Get Hss For TelURI " + ConfigUtils.getStackTraceString(e));
        }
    }

    @Override
    public void parseResponse() {
        if (null != this.responseStream) {
            try {
                this.username = HSSParserUtils.getUserName(this.responseStream);
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUsername() {
        return username;
    }
}
