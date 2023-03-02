package com.tcs.oci.commandObj;

import com.tcs.oci.execution.BaseImpl;
import com.tcs.oci.utils.Crypt;
import com.tcs.oci.utils.OCIConstants;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Map;

public class AuthCommand extends BaseOci {
    private static Logger LOGGER = LogManager.getLogger(AuthCommand.class);

    public AuthCommand(BaseImpl implObj, Map<String, String> paramList, String xmlName) throws OciException, IOException {
        super(implObj, "auth", xmlName);
        try {
            Document doc = XmlDocHandlerUpdated.getXmlDomObject(OCIConstants.authCommand);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", this.getAsUserName(), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "signedPassword", Crypt.getNonceHash(this.getAsPassword(),
                    paramList.get("nonce")), 0);
            this.setRequestDoc(doc);
        } catch (Exception e) {
            LOGGER.error("Exception in AuthCommand" + ConfigUtils.getStackTraceString(e));
            throw new OciException("OCI Exception during Authentication OCI");
        }

    }
}