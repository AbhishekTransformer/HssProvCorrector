package com.tcs.dns.query;

import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DNSClient {
	private static Logger LOGGER = LogManager.getLogger(DNSClient.class);
	private static final String RESOLVER_ADDRESS_TEST = "62.135.182.42";
	private static final String RESOLVER_ADDRESS_PROD = "62.135.183.91";

	private static String parseRegex(String regex) {
		String trans1 = null;
		try {
			trans1 = regex.substring(regex.indexOf("$!") + "$!".length(), regex.lastIndexOf("!"));
		} catch (StringIndexOutOfBoundsException e) {
			LOGGER.error(e.getStackTrace());
			return null;
		}

		LOGGER.info("DNS Record after transformation : " + trans1);
		if (!trans1.contains(";"))
			return trans1;

		String[] publicIds = trans1.split(";");
		LOGGER.info("DNS Record after splitting : " + publicIds);

		for (String publicId : publicIds)
			if (publicId.contains("@"))
				return publicId.substring(publicId.indexOf("+"));

		return null;
	}

	private static String queryDNS(String DN) {

		String RESOLVER_ADDRESS = RESOLVER_ADDRESS_PROD;
		if (Environment.TEST.str().equals(ConfigUtils.environment))
			RESOLVER_ADDRESS = RESOLVER_ADDRESS_TEST;

		try {
			Resolver resolver = new SimpleResolver(RESOLVER_ADDRESS);
			resolver.setTimeout(Duration.ofSeconds(3));
			Lookup.setDefaultResolver(resolver);
		} catch (UnknownHostException e) {
			LOGGER.info("Host not found : " + e);
		}

		Lookup.setDefaultCache(new Cache(), DClass.IN);
		Lookup lookup = null;
		try {
			lookup = new Lookup(DN, Type.NAPTR);
		} catch (TextParseException e) {
			LOGGER.error("Text Parse Exception : " + e);
		}

		List<Record> records = null;
		try {
			records = Arrays.asList(lookup.run());
		} catch (NullPointerException e) {
			LOGGER.error("No record found for : " + DN);
		}
		if (lookup.getResult() == Lookup.SUCCESSFUL && records != null) {
			if (records.size() == 1) {
				LOGGER.info("DNS Record fetched succesfully : " + records);

				NAPTRRecord naptrRecord = (NAPTRRecord) records.get(0);
				String regex = naptrRecord.getRegexp();

				return parseRegex(regex);

			} else {
				LOGGER.info("More than 1 DNS record : " + records);
//				List<String> naptrRecords = records.stream().map(NAPTRRecord.class::cast).map(NAPTRRecord::getRegexp)
//						.map(record -> record.substring(record.indexOf("$!") + "$!".length(), record.lastIndexOf("!\"")))
//						.collect(Collectors.toList());
			}
		} else {
			System.out.println("Lookup failed for : "+DN);
		}
		return null;
	}

	public List<String> search(List<String> msisdns) {
		List<String> publicIds = msisdns.stream().map(DNSClient::queryDNS).filter(Objects::nonNull).distinct().collect(Collectors.toList());
		LOGGER.info("Result : "+publicIds);
		return publicIds;
	}

	public static void main(String[] args) {
		System.out.println("Started client");
		String regex = "!^.*$!sip:akut1grethe@vk666668.hvoip.dk!";
		String parsed = parseRegex(regex);
		System.out.println("Parsed response = "+parsed);
	}
}
