package com.tcs.hss.client;

import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.Environment;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public abstract class BaseRequest {
	private static Logger LOGGER = LogManager.getLogger(BaseRequest.class);
	private CloseableHttpClient httpClient;
	private Document requestDoc;
	private String xmlName;
	private String cai3gAction;
	public InputStream responseStream;
	public String response;

	BaseRequest(String xmlName, String cai3gAction) {
		httpClient = HttpClientBuilder.create()
//				.setProxy(HttpHost.create("http://localhost:8080"))
				.build();
		this.xmlName = xmlName;
		this.cai3gAction = cai3gAction;
		setRequestDoc(createRequestDoc());
	}

	public Document getRequestDoc() {
		return requestDoc;
	}

	public void setRequestDoc(Document requestDoc) {
		this.requestDoc = requestDoc;
	}

	public Document createRequestDoc() {
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			LOGGER.info("xml Name : " + xmlName);
			File inputFile = new File(xmlName);
			doc = docBuilder.parse(inputFile);
		} catch (Exception e) {
			LOGGER.info(ConfigUtils.getStackTraceString(e));
		} finally {
			this.requestDoc = doc;
		}
		return doc;
	}
	public String getRequestStr() {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String request = "";
		try {
			transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(this.requestDoc), new StreamResult(writer));
			request = writer.getBuffer().toString();
			LOGGER.info("Str : " + request);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return request;
	}
    public void execute(){

		HttpPost httppost;
		if (Environment.PROD.str().equalsIgnoreCase(ConfigUtils.environment)) {
//			httppost = new HttpPost("http://192.168.156.203:8080/CAI3G1.2/services/CAI3G1.2");
			//KHEDA10.ip.tdk.dk: http://10.219.181.7:8080/CAI3G1.2/services/CAI3G1.2  to use
//			httppost = new HttpPost(ConfigUtils.edaProdUrl);
			LOGGER.info("Production EDA chosen: http://10.219.181.7:8080/CAI3G1.2/services/CAI3G1.2");
//			System.out.println("Production EDA chosen: "+ConfigUtils.edaProdUrl);
			httppost = new HttpPost("http://10.219.181.7:8080/CAI3G1.2/services/CAI3G1.2");
			
		} else if (Environment.TEST.str().equalsIgnoreCase(ConfigUtils.environment)) {
//			httppost = new HttpPost("http://10.117.108.119:8080/CAI3G1.2/services/CAI3G1.2");
//			httppost = new HttpPost("http://10.219.149.247:8080/CAI3G1.2/services/CAI3G1.2");
//			httppost = new HttpPost("http://10.219.149.231:8080/CAI3G1.2/services/CAI3G1.2");
			httppost = new HttpPost(ConfigUtils.edaTestUrl);
			LOGGER.info("Test Environment EDA chosen: "+ConfigUtils.edaTestUrl);
			System.out.println("Test Environment EDA chosen: "+ConfigUtils.edaTestUrl);
		} else {
			httppost = new HttpPost("http://192.168.156.203:8080/CAI3G1.2/services/CAI3G1.2");
		}

		String request = getRequestStr();
		LOGGER.info("HSS Request : " + request);
		LOGGER.info(cai3gAction);
		try {
			httppost.addHeader("SOAPAction", "\"CAI3G#" + cai3gAction + "\"");
			LOGGER.info("header is "+httppost.getAllHeaders());
			String charSet = "utf-8";
			StringEntity reqEntity = new StringEntity(request, ContentType.create("text/xml", charSet));
			LOGGER.info("request entity "+reqEntity.toString());
			httppost.setEntity(reqEntity);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(HttpResponse httpResponse) throws HttpResponseException, IOException {
					HttpEntity responseEntity = httpResponse.getEntity();
    					if (responseEntity != null) {
    						//String response = EntityUtils.toString(responseEntity); //can comment later
    						//LOGGER.info("Hss Response in string :"+response); // can comment later
    						responseStream = responseEntity.getContent();
    						InputStreamReader isReader = new InputStreamReader(responseStream);
    						BufferedReader reader = new BufferedReader(isReader);
    						String line = null;
    						String message = new String();
    						StringBuffer sb = new StringBuffer();
    						while(reader.ready()) {
    							line = reader.readLine();
    							LOGGER.info("Hss Response reader line : " + line);
    							message += line;
    						}
    						LOGGER.info("Hss Response reader message : " + message);
    						LOGGER.info("Hss Response reader: " + reader);
    						//String str = reader.readLine();
    						//LOGGER.info("Hss Response in string : " + str);
    						/*while((str = reader.readLine())!=null) {
    							sb.append(str);
    						}*/
    						//LOGGER.info("Hss Response in string : " + sb.toString());
    						LOGGER.info("Hss Response before parseResponse : " + responseStream);
							parseResponse();
							return ""; //Can return response or stream from here
						} else {
							throw new IOException("No HTTP Response Entity found");
						}
				}
			};
			httpClient.execute(httppost, responseHandler); // can return response from here.
			LOGGER.info("Hss Response : " + responseStream);
		} catch (Exception e) {
			LOGGER.info(ConfigUtils.getStackTraceString(e));
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				LOGGER.info(ConfigUtils.getStackTraceString(e));
			}
		}
    }
    public abstract void parseResponse();
}
