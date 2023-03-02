package com.tcs.hss.client;

import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.XmlDocHandlerUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class DeleteHssRequest extends BaseRequest {
	private static Logger LOGGER = LogManager.getLogger(DeleteHssRequest.class);

	public DeleteHssRequest(String sessionId, String associationId) {
		super(ConfigUtils.HssDeleteRequest, Cai3gAction.DELETE.str());
		try {
			Document doc = this.getRequestDoc();
			doc = XmlDocHandlerUpdated.setTagInXml(doc, "cai3:SessionId", sessionId, 0);
			doc = XmlDocHandlerUpdated.setTagInXml(doc, "associationId", associationId, 0);
			this.setRequestDoc(doc);
		} catch (Exception e) {
			LOGGER.error("Exception in delete Hss " + ConfigUtils.getStackTraceString(e));
		}
	}

	@Override
	public void parseResponse() {  // TODO : make sure to consider the scenario where hss profile does not exist or there is any error in get, as if not consider we may go and fix this user considering that user profile does not have public ids.
		//throw all kind of exceptions as user defined exception so that unexpected issues can be considered
		if (null != this.responseStream) {
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLEventReader reader;
			try {
				reader = xmlInputFactory.createXMLEventReader(this.responseStream);
				while (reader.hasNext()) {
					XMLEvent nextEvent = reader.nextEvent();
					if (nextEvent.isStartElement()) {
					    StartElement startElement = nextEvent.asStartElement();
					    if(startElement.getName().getLocalPart().equals("errorcode")) {
							nextEvent = reader.nextEvent();
							LOGGER.info("Some fault reason during delete HSS profile");
							LOGGER.info("Error code : " + nextEvent.asCharacters().getData());
							System.out.println("Some fault reason during delete HSS profile");
							System.out.println("Error code : " + nextEvent.asCharacters().getData());

						}
						if (startElement.getName().getLocalPart().equals("errormessage")) {
							nextEvent = reader.nextEvent();
							LOGGER.info("Some fault reason during delete HSS profile");
							LOGGER.info("Error message : " + nextEvent.asCharacters().getData());
							System.out.println("Some fault reason during delete HSS profile");
							System.out.println("Error code : " + nextEvent.asCharacters().getData());

						}

						if (startElement.getName().getLocalPart().equals("faultreason")) {
							LOGGER.info("Some fault reason during get");
							System.out.println("Fault resason");
						}

					}
				}
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
