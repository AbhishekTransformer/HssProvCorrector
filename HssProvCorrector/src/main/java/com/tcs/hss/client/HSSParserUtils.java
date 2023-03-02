package com.tcs.hss.client;

import com.tcs.exceptions.Exception1097;
import com.tcs.exceptions.ExceptionAssociationNotDefined;
import com.tcs.parser.utils.HSS_ERROR_CODE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HSSParserUtils {
	private static Logger LOGGER = LogManager.getLogger(HSSParserUtils.class);

	public static String getLoginSessionId(InputStream is) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLEventReader reader = xmlInputFactory.createXMLEventReader(is);
		while (reader.hasNext()) {
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if (startElement.getName().getLocalPart().equals("sessionId")) {
					nextEvent = reader.nextEvent();
					String sessionId = nextEvent.asCharacters().getData();
					LOGGER.info("sessionId : " + sessionId);
					return sessionId;
				}
			}
		}
		return null;
	}

	public static Map<String, List<?>> getPublicIdValues(InputStream is) throws XMLStreamException, Exception1097, ExceptionAssociationNotDefined {
		List<String> publicIdList = new ArrayList<String>();
		List<PrivateProfile> privateIdList = new ArrayList<>();
		Map<String, List<?>> map = new HashMap<>();
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLEventReader reader = xmlInputFactory.createXMLEventReader(is);
		PrivateProfile profile = null;
		while (reader.hasNext()) {
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				LOGGER.info("Public Id Name  new : " + startElement.getName());
				LOGGER.info(" nextEvent : " + nextEvent);
				switch (startElement.getName().getLocalPart()) {
					case "publicData":
						Attribute publicIdValue = startElement.getAttributeByName(new QName("publicIdValue"));
						LOGGER.info(" publicIdValue  new : " + publicIdValue);
						if (publicIdValue != null) {
							LOGGER.info(" PUBLIC ID : " + publicIdValue.getValue());
							publicIdList.add(publicIdValue.getValue());
						}
						break;
					case "privateUser":
						Attribute privateUserId = startElement.getAttributeByName(new QName("privateUserId"));
						LOGGER.info("PRIVATE ID : " + privateUserId.getValue());
						profile = new PrivateProfile(privateUserId.getValue(), null);
						break;
					case "msisdn":
						nextEvent = reader.nextEvent();
						String msisdn = nextEvent.asCharacters().getData();
						profile.setMsisdn(msisdn);
						privateIdList.add(profile);
						profile = null;
						break;
					case "errorcode":
						nextEvent = reader.nextEvent();
						if (nextEvent.asCharacters().getData().equals(HSS_ERROR_CODE.ERROR_1097.get()))
							throw new Exception1097();
						if (nextEvent.asCharacters().getData().equals(HSS_ERROR_CODE.ASSOCIATION_NOT_DEFINED.get()))
							throw new ExceptionAssociationNotDefined();
						break;
					case "faultreason":
						LOGGER.info("Some fault reason during get");
						break;
				}

			}
		}

		map.put("privateIdList", privateIdList);
		map.put("publicIdList", publicIdList);

		return map;
	}

	public static String getUserName(InputStream is) throws XMLStreamException {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLEventReader reader = xmlInputFactory.createXMLEventReader(is);
		while (reader.hasNext()) {
			XMLEvent nextEvent = reader.nextEvent();
			if (nextEvent.isStartElement()) {
				StartElement startElement = nextEvent.asStartElement();
				if (startElement.getName().getLocalPart().equals("associationId")) {
					nextEvent = reader.nextEvent();
					String associationId = nextEvent.asCharacters().getData();
					LOGGER.info("associationId : " + associationId);
					return associationId;
				}
			}
		}
		return null;
	}
}
