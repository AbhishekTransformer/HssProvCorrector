package com.tcs.parser.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NSLookup {
    private static Logger LISTENER_LOGGER = LogManager.getLogger(NSLookup.class);
    private HttpURLConnection connection = null;

    public String getAS(String username) {

        try {
            String urlString;
            if (Environment.PROD.str().equalsIgnoreCase(ConfigUtils.environment)) {
                urlString = "http://khk23dsi3.ip.tdk.dk";
            } else if (Environment.TEST.str().equalsIgnoreCase(ConfigUtils.environment)) {
                urlString = "http://khk9dsi12.ip.tdk.dk";
            } else {
                urlString = "http://khk9dsi12.ip.tdk.dk";
            }

            String urlComplete = urlString + "/servlet/LocateUser?url=" + username;
            URL url = new URL(urlComplete);
            LISTENER_LOGGER.info("NS url " + urlComplete);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            LISTENER_LOGGER.info("NS Response code  : " + responseCode);
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer responseBuffer = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    responseBuffer.append(inputLine);
                }
                LISTENER_LOGGER.info("response: " + responseBuffer);
                in.close();

                if (null != responseBuffer && !(responseBuffer.equals(""))) {
                    LISTENER_LOGGER.info(" in response buffer");
                    try {
                        String response = responseBuffer.toString();
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(new InputSource(new StringReader(response)));
                        boolean userIdExists = getNSResponse(doc);
                        if(userIdExists)
                        	{
                        		System.out.println(userIdExists);
                        		String clusterId = getCluster(doc);
                        		LISTENER_LOGGER.info("ClusterId " + clusterId);
                        		return clusterId;
                        	}
                        LISTENER_LOGGER.info("UserId doesnot exist Hence user not found, and ClusterId is null");
                        return null;              
                    } catch (Exception e) {
                        LISTENER_LOGGER.error(" Exception while processing HTTP Response" + ConfigUtils.getStackTraceString(e));
                        return "Exception while processing HTTP Response";
//                        return null;
                    }
                }
            } else {
                LISTENER_LOGGER.error(" Negative Response or Response Timeout HTTPRequestDispatcher");
                return null;
            }
			}catch (Exception e) {
				LISTENER_LOGGER.error(" Exception while sending to HTTPRequestDispatcher \n "+ConfigUtils.getStackTraceString(e));
				return null;
			}
			finally{
				if (null != connection) {
					connection.disconnect();
				}
			}
			return null;
	    }
	    private String getCluster(Document doc) {
            NodeList nList = XmlDocHandlerUpdated.getNodes(doc, "com.broadsoft.protocols.nsportal.ApplicationServerData");
            Node node = nList.item(0);
//            System.out.println(nList);
//            System.out.println(node);
            if (null != node) {
                NamedNodeMap attributes = node.getAttributes();
                Node attr = attributes.getNamedItem("address");
                if (attr != null)
                	System.out.println(attr);
                    return attr.getTextContent();
            }
            return null;

        }
	    private boolean getNSResponse(Document doc) {
            NodeList nList = XmlDocHandlerUpdated.getNodes(doc, "com.broadsoft.protocols.nsportal.Error");
//            System.out.println(nList);
            Node node = nList.item(0);
//            System.out.println(node);
            if (null != node) {
                NamedNodeMap attributes = node.getAttributes();
                Node attr = attributes.getNamedItem("detail");
                if (attr != null)
                	System.out.println(attr.getTextContent());
                    if(attr.getTextContent().equals("Unknown URL"))
                    	return false;
                    	
            }
            return true;

        }
	    public static void main (String args[]){
	    	NSLookup ns = new NSLookup();
	    	ns.getAS("1laege1adam@vk666668.hvoip.dk");
	    }
}
