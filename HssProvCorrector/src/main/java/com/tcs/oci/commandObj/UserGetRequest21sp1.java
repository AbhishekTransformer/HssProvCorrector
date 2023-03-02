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

public class UserGetRequest21sp1 extends BaseOci {
    private static Logger LOGGER = LogManager.getLogger(UserGetRequest21sp1.class);

    public UserGetRequest21sp1(BaseImpl implObj, Map<String, String> paramList, String xmlName) throws OciException, IOException {
        super(implObj, "UserGetRequest21sp1", xmlName);
        try {
            Document doc = this.getRequestDoc();
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", paramList.get("userId"), 0);
            this.setRequestDoc(doc);
        } catch (Exception e) {
            LOGGER.error("----Error in UserGetRequest21sp1 -----" + ConfigUtils.getStackTraceString(e));
            throw new OciException("OCI Exception during UserGetRequest21sp1 OCI");
        }
    }
}

