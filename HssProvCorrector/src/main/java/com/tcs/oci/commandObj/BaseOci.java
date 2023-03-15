package com.tcs.oci.commandObj;

/*File Name: BaseOci.class
 Author : m62167
 Company : TCS
 Description : Basic class template from which all Oci classes inherit from.    

 */

import com.XmlUtils.XmlDocHandler;
import com.tcs.oci.execution.BaseImpl;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;


public class BaseOci  {
	private static Logger LOGGER = LogManager.getLogger(BaseOci.class);
	private Socket selfSocket;
	protected Document requestDoc;
	protected Document responseDoc;
	protected Document errorResponseDoc;
	private String asUserName;
	private String asPassword;
	private BaseImpl implObj;
	protected String ociCommandName;
	static boolean ALARM_STATE = false;

	public BaseOci(BaseImpl implObj, String ociCommandName, String xmlName) throws OciException, IOException {

		this.selfSocket = implObj.getSocket();
		this.asUserName = implObj.getAsUserName();
		this.asPassword = implObj.getAsPassword();
		this.ociCommandName = ociCommandName;
		this.implObj = implObj;
		Document doc = null;
		FileInputStream inputFile = null; //added
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();

			//if (this.notification.isEmpty()) {
				System.out.println(xmlName);
		       File inputFile1 = new File(xmlName);   //correct one
		       inputFile = new FileInputStream(inputFile1); //added
//		       InputStream inputFile = new FileInputStream(xmlName); //new
		       //InputSource is = new InputSource();
		       //is.setCharacterStream(inputFile.get);
				doc = docBuilder.parse(inputFile);

			/*} else {
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(this.notification));
				doc = docBuilder.parse(is);
			}*/
		} catch (Exception e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
			throw new OciException("Error in picking Oci file");
		} finally {
			this.requestDoc = doc;
			inputFile.close();
		}
	}

	public BaseImpl getImplObj() {
		return implObj;
	}

	public void setImplObj(BaseImpl implObj) {
		this.implObj = implObj;
	}

	public Socket getSelfSocket() {
		return selfSocket;
	}

	public String getAsUserName() {
		return asUserName;
	}

	public String getAsPassword() {
		return asPassword;
	}



	public Document getRequestDoc() {
		return this.requestDoc;
	}

	public void setRequestDoc(Document requestDoc) {
		this.requestDoc = requestDoc;
	}

	public Document getResponseDoc() {
		return this.responseDoc;
	}

	public void setResponseDoc(Document responseDoc) {
		this.responseDoc = responseDoc;
	}

	public boolean execute() throws OciException{
		boolean status = false;
		try {

			String requestContent = XmlDocHandler.getDomFileContent(this.requestDoc);
	
			requestContent = requestContent.replace("\r\n", "\n");
			requestContent = requestContent.replace(
							"<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>",
							"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
			
			if(requestContent.contains("[phoneNumber]"))
			{
//				LOGGER.info("Able to find it here [phoneNumber]" );

//				LOGGER.info("Able to replace, after replace: "+requestContent);
				
				requestContent = requestContent.replace("<alternateEntry01>\n"
						, "");
				 requestContent = requestContent.replace("<phoneNumber>[phoneNumber]</phoneNumber>\n"
							, "");
				 requestContent = requestContent.replace("</alternateEntry01>\n"
							, "");
			}	
			
			if(requestContent.contains("<phoneNumber xsi:nil=\"true\"/>\n"))
			{
//				LOGGER.info("Able to find it here nil one" );

//				LOGGER.info("Able to replace, before replace: "+requestContent);
				
				requestContent = requestContent.replace("<alternateEntry01>\n"
						, "");
				 requestContent = requestContent.replace("<phoneNumber xsi:nil=\"true\"/>\n"
							, "");
				 requestContent = requestContent.replace("</alternateEntry01>\n"
							, "");
			}
			
			
			PrintWriter out = new PrintWriter(this.getSelfSocket().getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(this.getSelfSocket().getInputStream()));
			out.println(requestContent);
			LOGGER.info("request content" +requestContent);
			
			String responseContent = "";
			int inputReceived = -1;
			int counter = 0;
		while (true) {
			inputReceived = in.read();
			if (inputReceived != -1) {
				responseContent += (Character.toString((char) inputReceived));
				if (responseContent.contains("</BroadsoftDocument>")) {
					LOGGER.info("response content" + responseContent);
					status = true;
					break;
				}
			} else {

				if (counter > 10) {
					LOGGER.error("----OCI response not found----------");
					break;
				}
				counter++;
			}
		}
			if(responseContent != null && !responseContent.isEmpty())
			{
				if(responseContent.contains("Error"))
					this.errorResponseDoc = XmlDocHandler.getXmlDomObject(responseContent);
				else
					this.responseDoc = XmlDocHandler.getXmlDomObject(responseContent);
			}
		} catch (Exception e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
			throw new OciException("Error");
		}
		HashMap<String, Document> requestDocMap = this.getImplObj()
				.getRequestDocMap();
		HashMap<String, Document> responseDocMap = this.getImplObj()
				.getResponseDocMap();
		try {
			requestDocMap.put(this.ociCommandName, this.requestDoc);
			this.getImplObj().setRequestDocMap(requestDocMap);
			responseDocMap.put(this.ociCommandName, this.responseDoc);
			responseDocMap.put("ErrorResponse", this.errorResponseDoc);
			this.getImplObj().setResponseDocMap(responseDocMap);
		} catch (Exception e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
			throw new OciException("Error");
		}
		return status;
	}
}
