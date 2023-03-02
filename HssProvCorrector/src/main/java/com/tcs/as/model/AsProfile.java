package com.tcs.as.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class AsProfile {

	// other public id value parameters
	private static Logger LOGGER = LogManager.getLogger("generalLogger");
	private UserProfile profile;
	private AlternateNumber alternateNumber;
	private SCAProfile scaProfile;

	public AlternateNumber getAlternateNumber() {
		return alternateNumber;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public void setAlternateNumber(AlternateNumber alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	public SCAProfile getScaProfile() {
		return scaProfile;
	}

	public void setScaProfile(SCAProfile scaProfile) {
		this.scaProfile = scaProfile;
	}

	public List<String> getLinePortPublicIdsForImsiRequest() {
		String lineportFromUserProfile = this.profile.getAccessDeviceEndpoint().getLinePort();

		LOGGER.info("LinePort UserProfile: " + lineportFromUserProfile);

		List<String> lineportFromSCAProfile = this.scaProfile.getEndpointTable().stream()
				.filter(entry -> "System".equals(entry.get(SCAEndpoint.DEVICE_LEVEL.str()))
						&& "Mobile".equals(entry.get(SCAEndpoint.DEVICE_NAME.str()))
						&& "Generic_Mobile".equals(entry.get(SCAEndpoint.DEVICE_TYPE.str()))
						&& entry.containsKey(SCAEndpoint.LINE_PORT.str())
						&& entry.get(SCAEndpoint.LINE_PORT.str()).contains("@")) // Filter LP number containing @
				.map(e -> e.get(SCAEndpoint.LINE_PORT.str()).substring(0,
						e.get(SCAEndpoint.LINE_PORT.str()).indexOf("@"))) // Collecting only LinePort number without @
				.filter(ip -> ip != null && ip.matches("[-+]?\\d*\\.?\\d+")) // collecting only numeric LinePort
				.collect(Collectors.toList());

		LOGGER.info("LinePort SCAprofile: " + lineportFromSCAProfile);

		if (lineportFromUserProfile.contains("@")) {
			String lp = lineportFromUserProfile.substring(0, lineportFromUserProfile.indexOf("@"));
			if (lp.matches("[-+]?\\d*\\.?\\d+")) // Collecting only numeric LinePort
				lineportFromSCAProfile.add(lp); // Same process as above for UserProfile LinePort
		}

		LOGGER.info("LinePort BothProfile: " + lineportFromSCAProfile);

		return lineportFromSCAProfile.stream().map(lp -> lp.startsWith("+") ? lp.substring(1) : lp)
				.map(lp -> lp.startsWith("45") ? lp : "45".concat(lp)).distinct().collect(Collectors.toList());

	}

	public List<String> getMsisdnForDNSQuery() {
		List<String> alternateNumbers = this.alternateNumber.getAlternateEntry().stream().map(no -> no.getPhoneNumber())
				.collect(Collectors.toList());
		LOGGER.info("Alternate Numbers : " + alternateNumbers);
		String phoneNumber = this.profile.getPhoneNumber();
		LOGGER.info("Phone Number from UserProfile : " + phoneNumber);

		alternateNumbers.add(phoneNumber);
		LOGGER.info("Final List : " + alternateNumbers);

		List<String> afterTransformation = alternateNumbers.stream()
				.map(no -> no.startsWith("+") ? no.substring(1) : no)
				.map(no -> no.startsWith("45") ? no : "45".concat(no)).distinct().map(no -> no.replaceAll("\\B", "."))
				.map(no -> new StringBuilder(no).reverse().toString()).map(no -> no.concat(".e164.arpa"))
				.collect(Collectors.toList());

		LOGGER.info("After Transformation : " + afterTransformation);

		return afterTransformation;
	}

}
