package com.tcs.as.client;

import com.tcs.as.model.AlternateNumber;
import com.tcs.as.model.AsProfile;
import com.tcs.as.model.SCAProfile;
import com.tcs.as.model.UserProfile;
import com.tcs.oci.execution.ExecuteUserAlternateNumbersGetRequest21;
import com.tcs.oci.execution.ExecuteUserGetRequest21sp1;
import com.tcs.oci.execution.ExecuteUserSharedCallAppearanceGetRequest21sp1;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsProfileClient {
	private static Logger LOGGER = LogManager.getLogger(AsProfileClient.class);

	public AsProfile getAsProfile(String clusterId, String userId) throws OciException {

		// execute OCis to fetch respective parameters from AS, set it in pojo and return
		//UserGetProfile -- phone number : tel , primary line public identity :sip , sip alias : sip
		//UserAlternateNumber -- tels
		//UserGetSCA  : fetch public identity : sip

		AsProfile userAsProfile = new AsProfile();
		try {
			UserProfile userProfile = new ExecuteUserGetRequest21sp1(clusterId).runExecution(userId);
			if (null != userProfile) {
				AlternateNumber alternateNumber = new ExecuteUserAlternateNumbersGetRequest21(clusterId).runExecution(userId);
				SCAProfile scaProfile = new ExecuteUserSharedCallAppearanceGetRequest21sp1(clusterId).runExecution(userId);
				userAsProfile.setProfile(userProfile);
				userAsProfile.setAlternateNumber(alternateNumber);
				userAsProfile.setScaProfile(scaProfile);
		        LOGGER.info(" Alternate Number in AsProfileClient "+alternateNumber);
		        LOGGER.info(" scaProfile"+ scaProfile);
		        
			} else {
				return null;
			}
		} catch (OciException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
			if ((e.getMessage()).contains("User Not Found")) {
				throw e;
			}
		}
		//TODO LATER  : imsi public identitiy , +45 primary line identitiy on AS, get imsi and check on HSS // fix is to delete the hss profile

		//if error in any of the oci, due to which result is not there , need to make sure an exception is thrown, so that don't go for a fix , manual intervention required.
		return userAsProfile;
	}
}
