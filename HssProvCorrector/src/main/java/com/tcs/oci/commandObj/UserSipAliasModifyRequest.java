package com.tcs.oci.commandObj;

import com.tcs.oci.execution.BaseImpl;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserSipAliasModifyRequest extends BaseOci {
    private static Logger LOGGER = LogManager.getLogger(UserSipAliasModifyRequest.class);

    public UserSipAliasModifyRequest(BaseImpl implObj, Map<String, String> paramList, List<String> SIPAliasList, String xmlName) throws OciException, IOException {
        super(implObj, "UserSipAliasModifyRequest", xmlName);
        try {
            Document doc = this.getRequestDoc();
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", paramList.get("userId"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sipAlias", SIPAliasList.get(0), 0);
            SIPAliasList.remove(0);
            if (null != SIPAliasList && SIPAliasList.size() != 0) {
                Node cloneNode = doc.getElementsByTagName("sipAlias").item(0);
                Node tagNode = doc.getElementsByTagName("sipAliasList").item(0);

                for (String sipAlias : SIPAliasList) {
                    Element element = (Element) cloneNode.cloneNode(true);
                    element.setTextContent(sipAlias);
                    tagNode.appendChild(element);
                }
            }
            this.setRequestDoc(doc);
        } catch (Exception e) {
            LOGGER.error("----Error in UserSipAliasModifyRequest -----" + ConfigUtils.getStackTraceString(e));
            throw new OciException("OCI Exception during UserSipAliasModifyRequest OCI");
        }
    }
}

