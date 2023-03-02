package com.tcs.oci.commandObj;

import com.tcs.oci.execution.BaseImpl;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Map;

public class UserPhoneNumberModifyRequest extends BaseOci {
    private static Logger LOGGER = LogManager.getLogger(UserPhoneNumberModifyRequest.class);

    public UserPhoneNumberModifyRequest(BaseImpl implObj, Map<String, String> paramList, String xmlName) throws OciException, IOException {
        super(implObj, "UserPhoneNumberModifyRequest", xmlName);
        try {
            Document doc = this.getRequestDoc();
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", paramList.get("userId"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "phoneNumber", paramList.get("phoneNumber"), 0);
            this.setRequestDoc(doc);
        } catch (Exception e) {
            LOGGER.error("----Error in UserPhoneNumberModifyRequest -----" + ConfigUtils.getStackTraceString(e));
            throw new OciException("OCI Exception during UserPhoneNumberModifyRequest OCI");
        }
    }
}

