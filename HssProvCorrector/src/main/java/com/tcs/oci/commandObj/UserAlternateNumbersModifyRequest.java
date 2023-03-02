//package com.tcs.oci.commandObj;
//
//import com.tcs.as.model.AlternateNumberEntry;
//import com.tcs.oci.execution.BaseImpl;
//import com.tcs.oci.utils.OciException;
//import com.tcs.parser.utils.ConfigUtils;
//import com.tcs.parser.utils.XmlDocHandlerUpdated;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import java.util.List;
//import java.util.Map;
//
//public class UserAlternateNumbersModifyRequest extends BaseOci {
//	private static Logger LOGGER = LogManager.getLogger(UserAlternateNumbersModifyRequest.class);
//	List<Integer> indexList = ConfigUtils.getStoreIndexList();
//	
//	public UserAlternateNumbersModifyRequest(BaseImpl implObj, Map<String, String> paramList,
//			List<AlternateNumberEntry> alternateNumberList, String xmlName) throws OciException {
//		super(implObj, "UserAlternateNumbersModifyRequest", xmlName);
//		
//		try {
//			Document doc = this.getRequestDoc();
//			doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
//			doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", paramList.get("userId"), 0);
//			doc = XmlDocHandlerUpdated.setTagInXml(doc, "distinctiveRing", paramList.get("distinctiveRing"), 0);
////            doc = XmlDocHandlerUpdated.setTagInXml(doc, "phoneNumber", alternateNumberList.get(0).getPhoneNumber(), 0);
//			// doc = XmlDocHandlerUpdated.setTagInXml(doc, "ringPattern",
//			// alternateNumberList.get(0).getRingPattern(),0);
//
//			// doc = XmlDocHandlerUpdated.setTagInXml(doc, "extension",
//			// paramList.get("extension"),0); //Right now nil:true only. Need to change
//			// logic in future, if required
//			// doc = XmlDocHandlerUpdated.setTagInXml(doc, "description",
//			// paramList.get("description"),0);
//			int entryNum = 1;
//			int counter = 0;
////			int entryNum = indexList.remove(0);
////            alternateNumberList.remove(0);
//			LOGGER.info("alternateNumberList in modify request : " + alternateNumberList.toString());
//			LOGGER.info("Store Index List in modify request  ConfigUtils: " + ConfigUtils.getStoreIndexList().toString());
//			LOGGER.info(" Index List in modify request : " + indexList.toString());
//			if (null != alternateNumberList && alternateNumberList.size() > 0) {
//				LOGGER.info("Inside if in UserALternateNumberModifyRequest");
//				Node tagNode = doc.getElementsByTagName("command").item(0);
//				for (AlternateNumberEntry alternatNumber : alternateNumberList) {
//					if (indexList.size()>0)
//					{
//						LOGGER.info(" Inside if before removing : " + indexList.toString()+ConfigUtils.getStoreIndexList().toString() );
//						entryNum = indexList.get(counter);
//						LOGGER.info(" Inside if after removing : " + indexList.toString()+"entryNum value: "+entryNum+ "storeIndexList: " +ConfigUtils.getStoreIndexList().toString());
//						counter++;
//					}
//					
//					LOGGER.info("variable entryNum which contains the index of alternate Number: "+entryNum);
//					Element element = (Element) doc.getElementsByTagName("alternateEntry01").item(0);
//					Element element2;
//					if (alternatNumber != null) {
//						
//						if (entryNum == 10) {
//							element2 = doc.createElement("alternateEntry" + entryNum);
//						} else {
//							element2 = doc.createElement("alternateEntry0" + entryNum);
//						}
//						NodeList childNodes = element.getChildNodes();
//						Node node;
//
//						for (int i = 0; i < childNodes.getLength(); i++) {
//							node = childNodes.item(i);
//							if ("phoneNumber".equalsIgnoreCase(node.getNodeName())) {
//								Element childelement = (Element) node.cloneNode(true);
//								childelement.setTextContent(alternatNumber.getPhoneNumber());
//								element2.appendChild(childelement);
//							}
//							/*
//							 * if ("ringPattern".equalsIgnoreCase(node.getNodeName())) { Element
//							 * childelement = (Element)node.cloneNode(true);
//							 * childelement.setTextContent(alternatNumber.getRingPattern());
//							 * element2.appendChild(childelement); } if
//							 * (("extension".equalsIgnoreCase(node.getNodeName())) ||
//							 * (("description").equalsIgnoreCase(node.getNodeName()))){ Element childelement
//							 * = (Element)node.cloneNode(true); element2.appendChild(childelement); }
//							 */
//						}
//						tagNode.appendChild(element2);
//
//					}
////					entryNum++;
//				}
//			}
//			this.setRequestDoc(doc);
//			indexList.clear(); 
//			LOGGER.info("After clearing list check: "+ ConfigUtils.getStoreIndexList()+"and IndexList: " +indexList);///after using this list we'll have to clear so that there is no data for the comming fixer
//		} catch (Exception e) {
//			System.out.println(ConfigUtils.getStackTraceString(e));
//			LOGGER.error("----Error in UserAlternateNumbersModifyRequest -----" + ConfigUtils.getStackTraceString(e));
//			throw new OciException("OCI Exception during UserAlternateNumbersModifyRequest OCI");
//		}
//	}
//}

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

public class UserAlternateNumbersModifyRequest extends BaseOci {
    private static Logger LOGGER = LogManager.getLogger(UserAlternateNumbersModifyRequest.class);

    public UserAlternateNumbersModifyRequest(BaseImpl implObj, Map<String, String> paramList, List<AlternateNumberEntry> alternateNumberList, String xmlName) throws OciException, IOException {
        super(implObj, "UserAlternateNumbersModifyRequest", xmlName);
        try {
            Document doc = this.getRequestDoc();
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", paramList.get("userId"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "distinctiveRing", paramList.get("distinctiveRing"), 0);
            doc = XmlDocHandlerUpdated.setTagInXml(doc, "phoneNumber", alternateNumberList.get(0).getPhoneNumber(), 0);
            //doc = XmlDocHandlerUpdated.setTagInXml(doc, "ringPattern", alternateNumberList.get(0).getRingPattern(),0);

            //doc = XmlDocHandlerUpdated.setTagInXml(doc, "extension", paramList.get("extension"),0); //Right now nil:true only. Need to change logic in future, if required
            //doc = XmlDocHandlerUpdated.setTagInXml(doc, "description", paramList.get("description"),0);
            int entryNum = 1;
            alternateNumberList.remove(0);

            if (null != alternateNumberList && alternateNumberList.size() > 0) {
                Node tagNode = doc.getElementsByTagName("command").item(0);
                for (AlternateNumberEntry alternatNumber : alternateNumberList) {
                    Element element = (Element) doc.getElementsByTagName("alternateEntry01").item(0);
                    Element element2;
                    entryNum++;
                    if (entryNum == 10) {
                        element2 = doc.createElement("alternateEntry" + entryNum);
                    } else {
                        element2 = doc.createElement("alternateEntry0" + entryNum);
                    }
                    NodeList childNodes = element.getChildNodes();
                    Node node;
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        node = childNodes.item(i);
                        if ("phoneNumber".equalsIgnoreCase(node.getNodeName())) {
                            Element childelement = (Element) node.cloneNode(true);
                            childelement.setTextContent(alternatNumber.getPhoneNumber());
                            element2.appendChild(childelement);
                        }
							/* if ("ringPattern".equalsIgnoreCase(node.getNodeName())) {
									 Element childelement = (Element)node.cloneNode(true);
									 childelement.setTextContent(alternatNumber.getRingPattern());
									 element2.appendChild(childelement);
								}
							 if (("extension".equalsIgnoreCase(node.getNodeName())) || (("description").equalsIgnoreCase(node.getNodeName()))){
								 Element childelement = (Element)node.cloneNode(true);
								 element2.appendChild(childelement);
							 }*/
                    }
                    tagNode.appendChild(element2);
                }
            }
            this.setRequestDoc(doc);
        } catch (Exception e) {
            System.out.println(ConfigUtils.getStackTraceString(e));
            LOGGER.error("----Error in UserAlternateNumbersModifyRequest -----" + ConfigUtils.getStackTraceString(e));
            throw new OciException("OCI Exception during UserAlternateNumbersModifyRequest OCI");
        }
    }
}


