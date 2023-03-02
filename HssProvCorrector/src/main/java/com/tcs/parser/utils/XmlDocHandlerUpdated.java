package com.tcs.parser.utils;


import com.XmlUtils.XmlDocHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XmlDocHandlerUpdated extends XmlDocHandler {
	private static Logger LOGGER = LogManager.getLogger(XmlDocHandlerUpdated.class);

	public static String getTagValue(Document doc, String tag) {
		List<String> tagValueList = new ArrayList<String>();
		try {
			tagValueList = XmlDocHandler.getXmlTagValues(doc, tag);
		} catch (NullPointerException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}
		if (tagValueList != null && tagValueList.size() != 0 && tagValueList.get(0) != null) {
			return tagValueList.get(0);
		} else {
			return "";
		}
	}

	public static String getXmlTagValue(Document doc, String tag, int index) {
		Node tagNode = null;
		try {
			tagNode = doc.getElementsByTagName(tag).item(index);
			if (tagNode != null)
				return tagNode.getTextContent();
			else
				return "";
			// transformer.transform(source, consoleResult);
		} catch (Exception e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
			return "";
		}

	}

	public static NodeList getNodes(Document doc, String tag) {
		NodeList tagNode = null;
		try {
			tagNode = doc.getElementsByTagName(tag);
			return tagNode;
			// transformer.transform(source, consoleResult);
		} catch (Exception e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
			return null;
		}
	}

	public static int isServiceAuth(String response) {
		Document doc = XmlDocHandlerUpdated.getXmlDomObject(response);
		NodeList a = XmlDocHandlerUpdated.getNodes(doc, "servicePackAuthorization");
		for (int i = 0; i < a.getLength(); i++) {
			Node b = a.item(i);
			if (b.getTextContent().contains("LegacyPBX")) {
				System.out.println("######asaasa######################");
				NodeList auth = null;
				if (b != null) {
					auth = b.getChildNodes();
				}
 	    		 if(auth!=null){
 	    			 for(int j = 0;j<auth.getLength();j++){
						 String tag = auth.item(j).getNodeName();
						 if (tag.equalsIgnoreCase("unauthorized")) {
							 return 0;
						 }
					 }
					 return 1;
				 }
			}
		}
		return 2;

	}

	public static void main(String args[]) {
		String response = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
				"    <BroadsoftDocument protocol=\"OCI\" xmlns=\"C\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" +
				"       <sessionId xmlns=\"\">'''+sessionId+'''</sessionId>\r\n" +
				"          <command xsi:type=\"GroupServiceModifyAuthorizationListRequest\" xmlns=\"\">" +
				"    <serviceProviderId>TESTTDCSP23</serviceProviderId>\r\n" +
				"    <groupId>VK666666</groupId>\r\n" +
				"    <servicePackAuthorization>\r\n" +
				"      <servicePackName>BTBCDesktopBasis_old_2017-02-14T18:10:39</servicePackName>\r\n" +
				"      <unauthorized>true</unauthorized>\r\n" +
				"    </servicePackAuthorization>\r\n" +
				"    <servicePackAuthorization>\r\n" +
				"      <servicePackName>BTBCDesktopBasis_old_2017-02-14T17:43:39</servicePackName>\r\n" +
				"      <unauthorized>true</unauthorized>\r\n" +
				"    </servicePackAuthorization>\r\n" +
				"    <servicePackAuthorization>\r\n" +
				"      <servicePackName>BTBCDesktopEkstra123</servicePackName>\r\n" +
				"      <unauthorized>true</unauthorized>\r\n" +
				"    </servicePackAuthorization>\r\n" +
				"    <servicePackAuthorization>\r\n" +
				"      <servicePackName>BTBCDesktopEkstra1234</servicePackName>\r\n" +
				"      <unauthorized>true</unauthorized>\r\n" +
				"    </servicePackAuthorization>\r\n" +
				"    <servicePackAuthorization>\r\n" +
				"      <servicePackName>BTBCDesktopEkstra12345</servicePackName>\r\n" +
				"      <unauthorized>true</unauthorized>\r\n" +
				"    </servicePackAuthorization>\r\n" +
				"    <servicePackAuthorization>\r\n" +
				"      <servicePackName>BTBCDesktopEkstra123456</servicePackName>\r\n" +
				"      <unauthorized>true</unauthorized>\r\n" +
				"    </servicePackAuthorization>\r\n" +
				"    <servicePackAuthorization>\r\n" +
				"      <servicePackName>BTBCDesktopBasis_TEMP_2017-02-14T18:08:52</servicePackName>\r\n" +
				"      <unauthorized>true</unauthorized>\r\n" +
				"    </servicePackAuthorization>\r\n" +
				"    <servicePackAuthorization>\r\n" +
				"      <servicePackName>LegacyPBX</servicePackName>\r\n" +
				"      <unauthorized>true</unauthorized>\r\n" +
				"    </servicePackAuthorization>\r\n" +
				" </command>\r\n" +
				"</BroadsoftDocument>";
		System.out.println(isServiceAuth(response));
		System.out.println("#######");
	}
}
	


