package com.tcs.vashlr.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VASHLRClient {
	private static Logger logger = LogManager.getLogger(VASHLRClient.class);

	public static VASHLRProfileResponse getHssProfile(String msisdn) {
		logger.info("Msisdn received for imsi find new: " + msisdn);
		//msisdn = "4561229150";
		GetIMSIRequest getHss = new GetIMSIRequest(msisdn);
		return getHss.executeRequest();
	}

	public List<String> getImsiValues(List<String> msisdnList) {
		logger.info("Msisdn received for imsi find : " + msisdnList);
		List<String> imsiValues = msisdnList.stream().map(VASHLRClient::getHssProfile).filter(Objects::nonNull).map(msisdn -> msisdn.getBody().getLookupByMsisdnResponse()
				.getLookupByMsisdnReturn().getImsi()).collect(Collectors.toList());
		logger.info("IMSI values received from vashlr for all msisdns: " + imsiValues);
		return imsiValues;
	}

	public static void main(String[] args) {
		VASHLRClient getHss = new VASHLRClient();
		getHss.getImsiValues(Arrays.asList("4562608433"));
	}

}
