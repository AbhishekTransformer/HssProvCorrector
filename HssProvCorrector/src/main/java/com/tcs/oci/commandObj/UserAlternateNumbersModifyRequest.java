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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserAlternateNumbersModifyRequest extends BaseOci {
	private static Logger LOGGER = LogManager.getLogger(UserAlternateNumbersModifyRequest.class);
//	List<Integer> indexList = ConfigUtils.getStoreIndexList();
	ArrayList<Integer> indexList = new ArrayList<>(); //3 jan
	public UserAlternateNumbersModifyRequest(BaseImpl implObj, Map<String, String> paramList,
			List<AlternateNumberEntry> alternateNumberList, String xmlName, int indexValue, int loopIndex) throws OciException, IOException {
		
		super(implObj, "UserAlternateNumbersModifyRequest", xmlName);
		LOGGER.info("Before adding in IndexList, IndexValue: "+ indexValue+"Indexlist : "+indexList);
		indexList.add(indexValue); //3 jan
		LOGGER.info("After adding in IndexList, IndexValue: "+ indexValue+"Indexlist : "+indexList);
		try {
			Document doc = this.getRequestDoc();
			doc = XmlDocHandlerUpdated.setTagInXml(doc, "sessionId", paramList.get("timeStamp"), 0);
			doc = XmlDocHandlerUpdated.setTagInXml(doc, "userId", paramList.get("userId"), 0);
			doc = XmlDocHandlerUpdated.setTagInXml(doc, "distinctiveRing", paramList.get("distinctiveRing"), 0);
			int entryNum = 1;
			LOGGER.info("alternateNumberList in modify request : " + alternateNumberList.toString());
			LOGGER.info("Store Index List in modify request  in original ConfigUtils: " + ConfigUtils.getStoreIndexList().toString());
			LOGGER.info(" single Index List in modify request : " + indexList.toString());
			if (null != alternateNumberList && alternateNumberList.size() > 0) {
				LOGGER.info("Inside if in UserALternateNumberModifyRequest");
				Node tagNode = doc.getElementsByTagName("command").item(0);
				if(alternateNumberList.size() >= loopIndex)
				{
					AlternateNumberEntry alternatNumber = alternateNumberList.get(loopIndex);

						LOGGER.info(" Inside if before removing : " + indexList.toString()+ConfigUtils.getStoreIndexList().toString() );
						entryNum = indexValue;
						LOGGER.info(" Inside if after removing : " + indexList.toString()+"entryNum value: "+entryNum+ "storeIndexList: " +ConfigUtils.getStoreIndexList().toString());
			
					
					LOGGER.info("variable entryNum which contains the index of alternate Number: "+entryNum);
					Element element = (Element) doc.getElementsByTagName("alternateEntry01").item(0);
					Element element2;
					if (alternatNumber != null) {
						
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
						}
						tagNode.appendChild(element2);

					}
				}
			}
			
			this.setRequestDoc(doc);
			indexList.clear(); 
			LOGGER.info("After clearing list check: "+ ConfigUtils.getStoreIndexList()+"and IndexList: " +indexList);///after using this list we'll have to clear so that there is no data for the comming fixer
		} catch (Exception e) {
			System.out.println(ConfigUtils.getStackTraceString(e));
			LOGGER.error("----Error in UserAlternateNumbersModifyRequest -----" + ConfigUtils.getStackTraceString(e));
			throw new OciException("OCI Exception during UserAlternateNumbersModifyRequest OCI");
		}
	}
}

