package com.tcs.oci.commandObj;

import com.tcs.as.model.AlternateNumberEntry;
import com.tcs.oci.execution.BaseImpl;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserAlternateNumbersModifyRequestNIL extends BaseOci {
    private static Logger LOGGER = LogManager.getLogger(UserAlternateNumbersModifyRequestNIL.class);
    List<Integer> tempIndex = ConfigUtils.getStoreIndexList();
    
    public UserAlternateNumbersModifyRequestNIL(BaseImpl implObj, Map<String, String> paramList, List<AlternateNumberEntry> alternateNumberList, String xmlName, int indexValue, int loopIndex) throws OciException, IOException {
        super(implObj, "UserAlternateNumbersModifyRequestNIL", xmlName);
        try {
            Document doc = this.getRequestDoc();
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", paramList.get("userId"), 0);

            int entryNum = 1;
            LOGGER.info("Temp Index :"+ tempIndex.toString());
            if (null != alternateNumberList && alternateNumberList.size() > 0) {
                Node tagNode = doc.getElementsByTagName("command").item(0);
                if(alternateNumberList.size() >= loopIndex) {
            		AlternateNumberEntry alternatNumber = alternateNumberList.get(loopIndex);

                    Element element = (Element) doc.getElementsByTagName("alternateEntry01").item(0);
                    Element element2;

                    entryNum = indexValue;

                    LOGGER.info("Temp Index, storeIndexList and entryNum  respectively:"+ tempIndex.toString() + ConfigUtils.getStoreIndexList() + entryNum);
                    if (entryNum == 10) {
                        element2 = doc.createElement("alternateEntry" + entryNum);
                    } else {
                        element2 = doc.createElement("alternateEntry0" + entryNum);
                    }
                    NodeList childNodes = element.getChildNodes();
                    Node node;
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        node = childNodes.item(i);
                        if (/*("extension".equalsIgnoreCase(node.getNodeName())) || (("description").equalsIgnoreCase(node.getNodeName())) || */("phoneNumber".equalsIgnoreCase(node.getNodeName()))/* || ("ringPattern".equalsIgnoreCase(node.getNodeName()))*/) {
                            Element childelement = (Element) node.cloneNode(true);
                            element2.appendChild(childelement);
                        }
                    }
                    tagNode.appendChild(element2);
                }
            }
            this.setRequestDoc(doc);
        }

         catch (Exception e) {
            LOGGER.error("----Error in UserAlternateNumbersModifyRequestNIL -----" + ConfigUtils.getStackTraceString(e));
            throw new OciException("OCI Exception during UserAlternateNumbersModifyRequestNIL OCI");
        }
    }
}
