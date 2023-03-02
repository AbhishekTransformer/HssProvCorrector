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

public class UserSharedCallAppearanceDeleteEndpointListRequest14 extends BaseOci {
    private static Logger LOGGER = LogManager.getLogger(UserSharedCallAppearanceDeleteEndpointListRequest14.class);

    public UserSharedCallAppearanceDeleteEndpointListRequest14(BaseImpl implObj, Map<String, String> paramList, String xmlName) throws OciException, IOException {
        super(implObj, "UserSharedCallAppearanceDeleteEndpointListRequest14", xmlName);
        try {
            Document doc = this.getRequestDoc();
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", paramList.get("userId"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "deviceLevel", paramList.get("deviceLevel"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "deviceName", paramList.get("deviceName"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "linePort", paramList.get("linePort"), 0);
            this.setRequestDoc(doc);
        } catch (Exception e) {
            LOGGER.error("----Error in UserSharedCallAppearanceDeleteEndpointListRequest14 -----" + ConfigUtils.getStackTraceString(e));
            throw new OciException("OCI Exception during UserSharedCallAppearanceDeleteEndpointListRequest14 OCI");
        }
    }
}

