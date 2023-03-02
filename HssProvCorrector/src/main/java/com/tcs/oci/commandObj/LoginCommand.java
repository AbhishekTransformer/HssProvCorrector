package com.tcs.oci.commandObj;

import com.tcs.oci.execution.BaseImpl;
import com.tcs.oci.utils.OCIConstants;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Map;

public class LoginCommand extends BaseOci {
    private static Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    public LoginCommand(BaseImpl implObj, Map<String, String> paramList, String xmlName) throws OciException, IOException {
        super(implObj, "login", xmlName);
        try {
            Document doc = XmlDocHandlerUpdated.getXmlDomObject(OCIConstants.loginCommand);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", this.getAsUserName(), 0);
            this.setRequestDoc(doc);
        } catch (Exception e) {
            LOGGER.error("Exception in LoginCommand" + ConfigUtils.getStackTraceString(e));
            throw new OciException("OCI Exception during Login OCI");
        }
    }
}