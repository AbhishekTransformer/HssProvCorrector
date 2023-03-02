package com.tcs.corrector.service;

import com.tcs.as.client.AsProfileClient;
import com.tcs.as.model.AlternateNumberEntry;
import com.tcs.as.model.AsProfile;
import com.tcs.as.model.SCAEndpoint;
import com.tcs.dns.query.DNSClient;
import com.tcs.exceptions.BadRequestException;
import com.tcs.generate.utils.FailureReportObj;
import com.tcs.hss.client.HssProfile;
import com.tcs.hss.client.HssSoapClient;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.dto.events;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.HSS_ERROR_CODE;
import com.tcs.vashlr.client.VASHLRClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

public class FixController {
	private static Logger LOGGER = LogManager.getLogger(FixController.class);
	private static DNSClient dnsClient = new DNSClient();
	private static VASHLRClient vashlrClient = new VASHLRClient();

	// TODO : implement exception handling
	public List<FailureReportObj> validateAndFix(List<events> failEventList) {
		// List<events> toBeRemovedList = new ArrayList<events> ();
		List<FailureReportObj> displayList = new ArrayList<FailureReportObj>();
		for (events ev : failEventList) {
			String clusterId = ev.getAsUrl();
			// clusterId = clusterId.substring(0, clusterId.indexOf(":"));
			clusterId = clusterId.split(":")[0];
			LOGGER.info("As url:::::: " + ev.getAsUrl() + ",     cluster Id : :::  " + clusterId);
			String userId = ev.getDocUserId();
			try {
				if (clusterId == null) {
					LOGGER.info("ClusterId not found Hence considering it as User not found on AS");
					processOrphanProfile(displayList, null, clusterId, userId);
				}
				else
					process(displayList, ev, clusterId, userId);
			} catch (OciException e) {
				// This OCIException is thrown from getAsProfile when a user is not found
				LOGGER.info(e.getMessage() + " Should be User not found");
//				try {
//					if(e.getMessage().toLowerCase() == "user not found")
//					{
//						processOrphanProfile(displayList, ev, clusterId, userId);
//					}
//				} catch (BadRequestException e1) {
//					LOGGER.info(e1.getMessage());
//				}
			}
			catch (BadRequestException e1) {
				LOGGER.info(e1.getMessage());
			}
		}
		return displayList;
	}

	public List<FailureReportObj> validateAndFix(String clusterId, String userId) throws BadRequestException {
		List<FailureReportObj> displayList = new ArrayList<FailureReportObj>();
		try {
			if (clusterId == null) {
				LOGGER.info("ClusterId not found Hence considering it as User not found on AS");
				processOrphanProfile(displayList, null, clusterId, userId);
			}
			else
				process(displayList, null, clusterId, userId);
		} catch (OciException e) {
			// This OCIException is thrown from getAsProfile when a user is not found
			LOGGER.info(e.getMessage() + " Should be User not found");  //here
			LOGGER.info(e.getMessage() + " In catch block");
//			if(e.getMessage().toLowerCase().contains("user not found"))
//			{
//				processOrphanProfile(displayList, null, clusterId, userId);
//			}
		}
		return displayList;
	}

	public void processOrphanProfile(List<FailureReportObj> displayList, events ev, String clusterId, String userId)
			throws BadRequestException {
		FailureReportObj displayObj = new FailureReportObj();
		HssSoapClient hssSoapClient = new HssSoapClient();
		HssProfile userHssProfile = hssSoapClient.getHssProfile(userId);
		
		LOGGER.info("fix value: "+ ConfigUtils.fix);
		
		if (userHssProfile != null) { // to fix session id = null
			if(userHssProfile.getPublicIdList() == null) {
				LOGGER.info("User Id doesn't exist in AS and HSS");		
				displayObj.setErrorMessage("Profile does not exist in AS and HSS");
				displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
				displayObj.setFailureReason("User Id doesn't exist in AS and HSS");
				displayList.add(displayObj);
			}
			else {	
			if (userHssProfile.getFault() != null && userHssProfile.getFault().isFault() == true
					&& userHssProfile.getFault().getErrorcode().equals(HSS_ERROR_CODE.ASSOCIATION_NOT_DEFINED.get())) {
				LOGGER.info(
						"User's BW profile was not found. Went to check if orphan but no HSS profile found. We are good here. User Id: "
								+ userId);
				throw new BadRequestException("User Id doesn't exist in AS and HSS");
			} else {
//				boolean fixValue = false; //temporary changes needs to change
//				LOGGER.info("Temporary Hard coded fixValue  Needs to be changed: ");
				if (ConfigUtils.fix) {
					displayObj.setEvents(ev);
					hssSoapClient.deleteHssProfile(userId);
					displayObj.setFailureReason("Orphan HSS Profile");
					displayObj.setErrorMessage("Profile does not exist in AS but exists on HSS");
					LOGGER.info(
							"User's BW profile was not found. Went to check if orphan but HSS profile found. Deleting the user Id: "
									+ userId);
					HssProfile userHssProfile1 = hssSoapClient.getHssProfile(userId);
					if (userHssProfile1.getFault() != null && userHssProfile1.getFault().isFault() == true
							&& userHssProfile1.getFault().getErrorcode()
									.equals(HSS_ERROR_CODE.ASSOCIATION_NOT_DEFINED.get()))
						displayObj.setFixStatus(FixResult.SUCCESS.str());
					else
						displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
					displayList.add(displayObj);
					System.out.println("Orphan HSS Profile");
				} else {
					LOGGER.info("Fixing off, orphan profile not processed");
					displayObj.setEvents(ev);
					displayObj.setFailureReason("Orphan HSS Profile");
					displayObj.setErrorMessage("Profile does not exist in AS but exists on HSS");
					displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
				}
			}
		}
	}
		else {
			LOGGER.info("Error while Getting Session Id, SessionId is null");		
			displayObj.setErrorMessage("Bad Connection");
			displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
			displayObj.setFailureReason("No Session ID found");
			displayList.add(displayObj);
		}
}

	private void process(List<FailureReportObj> displayList, events ev, String clusterId, String userId)
			throws OciException {
		FailureReportObj displayObj = new FailureReportObj();
		AsProfileClient asProfileClient = new AsProfileClient();
		List<String> imsiReceivedFromVASHlr = null;
		List<String> publicIdsFromDNS = null;
		AsProfile asProfile = asProfileClient.getAsProfile(clusterId, userId);
		if (asProfile != null)
		{
			List<String> publicIdsForImsi = asProfile.getLinePortPublicIdsForImsiRequest();
			List<String> numbersForDnsQuery = asProfile.getMsisdnForDNSQuery();
			imsiReceivedFromVASHlr = vashlrClient.getImsiValues(publicIdsForImsi);
			publicIdsFromDNS = dnsClient.search(numbersForDnsQuery);
			LOGGER.info("FixController:userId: " + userId);
			LOGGER.info("FixController:imsiReceivedFromVASHlr: " + imsiReceivedFromVASHlr);
			LOGGER.info(" Alternate Number in FixController: " + asProfile.getAlternateNumber());
		}
		HssValidator hssValidator = new HssValidator();
		HssSoapClient hssSoapClient = new HssSoapClient();
		HssProfile userHssProfile = hssSoapClient.getHssProfile(userId);
		LOGGER.info("FixController:userHssProfile: " + userHssProfile);
		boolean isRestRequest = null == ev;

		if (userHssProfile != null) // to handle userHssProfile = null
		{
			if (userHssProfile.getFault() != null && userHssProfile.getFault().isFault()) {
				String failureReason = "";
				String errorMessage = "";
				if (userHssProfile.getFault().getErrorcode().equals(HSS_ERROR_CODE.ERROR_1097.get())) {
					hssSoapClient.deleteHssProfile(userId);
					failureReason = "Error 1097";
					errorMessage = "1097 Error in HSS profile";
					LOGGER.info("Error 1097 occured for user Id: " + userId);
				}
				if (userHssProfile.getFault().getErrorcode().equals(HSS_ERROR_CODE.ASSOCIATION_NOT_DEFINED.get())) {
					failureReason = "Profile does not exist in HSS";
					errorMessage = "Association not defined in HSS";
					LOGGER.info("Association id is not defined in HSS: " + userId);
				}
				
				fixRevalidateAndCheckIncompProv(displayObj, ev, asProfile, hssValidator, userHssProfile, failureReason,
						errorMessage, clusterId, userId, isRestRequest);
				displayList.add(displayObj);
			} else {
				ErrorReporter reporter = hssValidator.checkpublicIdsExists(userId, asProfile, userHssProfile,
						imsiReceivedFromVASHlr, publicIdsFromDNS);
				if (reporter.hasErrorsExcept(ErrorReporter.ASMISSING, ErrorReporter.ASZEROPUBLICIDS,
						ErrorReporter.HSSMISSINGIMSI, ErrorReporter.HSSEXTRAIMSI)
						|| (isRestRequest && reporter.hasErrors())) {
					fixRevalidateAndCheckIncompProv(displayObj, ev, asProfile, hssValidator, userHssProfile,
							reporter.getAllFailureReasons(), reporter.getAllErrorMessages(), clusterId, userId, true);
					displayList.add(displayObj);
				} else if (reporter.hasErrors()) {
					displayObj.setErrorMessage(reporter.getAllErrorMessages());
					displayObj.setFailureReason(reporter.getAllFailureReasons());
					displayObj.setFixStatus(FixResult.NOT_ATTEMPTED.str());
					displayList.add(displayObj);
					LOGGER.info("Fail skipped: But error exists : " + reporter);
				} else {
					LOGGER.info("Fail skipped: Already fixed :" + userId); // This should be skipped ? As due to some
																			// errors
					// in
					// fetching profiles also, this map size can be
					// 0
				}
			}
		}
	}
	

	private void reValidate(String userId, FailureReportObj displayObj, AsProfile asProfile, HssValidator hssValidator,
			HssProfile oldHssProfile, boolean isRestRequest) {
		try {
			Thread.sleep(ConfigUtils.revalidatingWaitingTime);
		} catch (InterruptedException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}
		HssSoapClient hssSoapClient = new HssSoapClient();
		HssProfile userHssProfile = hssSoapClient.getHssProfile(userId);
		List<String> imsiReceivedFromVASHlr = null;
		List<String> publicIdsFromDNS = null;
		if (asProfile != null)
		{
			List<String> msisdnForImsi = asProfile.getLinePortPublicIdsForImsiRequest();
			List<String> numbersForDNS = asProfile.getMsisdnForDNSQuery();
			imsiReceivedFromVASHlr = vashlrClient.getImsiValues(msisdnForImsi);
			publicIdsFromDNS = dnsClient.search(numbersForDNS);
		}
		
		ErrorReporter reporter = hssValidator.checkpublicIdsExists(userId, asProfile, userHssProfile,
				imsiReceivedFromVASHlr, publicIdsFromDNS); // is imsi list required while revalidation??

		if (userHssProfile != null && oldHssProfile != null) { // to handle session Id = null
			LOGGER.info("Old Imsi : " + oldHssProfile.getImsi());
			LOGGER.info("New Imsi : " + userHssProfile.getImsi());
			if (userHssProfile.getImsi() != null && oldHssProfile.getImsi() != null)
				if (!userHssProfile.getImsi().equals(oldHssProfile.getImsi()))
					displayObj.setFailureReason(displayObj.getFailureReason() + ", Imsi error");
		}
		if (reporter.hasErrorsExcept(ErrorReporter.ASMISSING, ErrorReporter.ASZEROPUBLICIDS)
				|| (isRestRequest && reporter.hasErrors())) {
			displayObj.setErrorMessage(reporter.getAllErrorMessages());
			displayObj.setFailureReason(reporter.getAllFailureReasons());
			displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());

			if (reporter.getFailedPublicIds(ErrorReporter.HSSMISSING) != null
					&& reporter.hasFailedPublicId(ErrorReporter.HSSMISSING, "tel")) {

				List<String> telUriToSearch = reporter.getFailedPublicIds(ErrorReporter.HSSMISSING, "tel");
				try {
					Map<String, String> result = telUriToSearch.parallelStream().collect(Collectors
							.toMap(Function.identity(), telUri -> hssSoapClient.getUsernameFromTelURI(telUri)));

					LOGGER.info("Mapping found : " + result);

					String mapString = result.keySet().stream().map(key -> key + "=" + result.get(key))
							.collect(Collectors.joining(", ", "{", "}"));

					displayObj.setFixStatus(FixResult.FAILED_INUSE.str());
					displayObj.setErrorMessage(reporter.getAllErrorMessages() + " Phone number mapping = " + mapString);
				} catch (Exception e) {
					LOGGER.error(e);
					System.out.println("Exception : ");
					e.printStackTrace();
					LOGGER.info("Fix Failed, Manual Intervention required " + userId);
					displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
				}
			}
			LOGGER.info(displayObj.getFixStatus() + " " + userId);
		} else {
			displayObj.setFixStatus(FixResult.SUCCESS.str());
			LOGGER.info("Fix Success " + userId);
		}
	}

	public void checkIncompleteProvisioning(String userId, FailureReportObj displayObj, AsProfile asProfile,
			HssValidator hssValidator, HssProfile oldHssProfile) {
		try {
			if (displayObj.getFixStatus().contains("Manual Intervention")) {
				String mobileSystem = "";
				if (asProfile.getProfile().getAccessDeviceEndpoint().getAccessDevice().getDeviceName().equals("Mobile")
						&& asProfile.getProfile().getAccessDeviceEndpoint().getLinePort() != null
						&& !asProfile.getProfile().getAccessDeviceEndpoint().getLinePort().isEmpty()) {
					mobileSystem = asProfile.getProfile().getAccessDeviceEndpoint().getLinePort();
				} else if (asProfile.getScaProfile().getEndpointTable() != null
						&& asProfile.getScaProfile().getEndpointTable().size() != 0) {
					List<Map<String, String>> endpointTable = asProfile.getScaProfile().getEndpointTable();
					for (Map<String, String> profileObj : endpointTable) {
						if (profileObj.get(SCAEndpoint.DEVICE_LEVEL.str()).equals("System")
								&& profileObj.get(SCAEndpoint.DEVICE_NAME.str()).equals("Mobile")) {

							mobileSystem = profileObj.get(SCAEndpoint.LINE_PORT.str());
						}
					}
				}
				if (mobileSystem != null && !mobileSystem.isEmpty()) {
					if (!asProfile.getProfile().getPhoneNumber().isEmpty()
							&& asProfile.getProfile().getPhoneNumber() != null
							&& mobileSystem.contains(asProfile.getProfile().getPhoneNumber())) {
						displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
					} else {
						String alternateNumber = "";
						List<AlternateNumberEntry> alternateEntry = asProfile.getAlternateNumber().getAlternateEntry();
						if (alternateEntry != null && alternateEntry.size() != 0) {
							for (AlternateNumberEntry alt : alternateEntry) {
								if (alt != null && alt.phoneNumber != null && !alt.phoneNumber.isEmpty()) {
									alternateNumber = alt.phoneNumber;
									break;
								}
							}
						}
						if (!alternateNumber.isEmpty() && alternateNumber != null
								&& mobileSystem.contains(alternateNumber)) {
							displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
						} else {
							displayObj.setFixStatus(FixResult.FAILED_INCOM_PROV.str());
						}
					}
				} else {
					displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
					LOGGER.info("Fix Failed, Manual Intervention required " + userId);
				}

			}
		} catch (Exception e) {
			LOGGER.error(e);
			System.out.println("Exception : ");
			e.printStackTrace();
			LOGGER.info("Fix Failed, Manual Intervention required " + userId);
			displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
		}
	}

	private void fixRevalidateAndCheckIncompProv(FailureReportObj displayObj, events ev, AsProfile asProfile,
			HssValidator hssValidator, HssProfile userHssProfile, String failureReason, String errorMessage,
			String clusterId, String userId, boolean isRestRequest) {
		displayObj.setEvents(ev);
		displayObj.setFailureReason(failureReason);
		displayObj.setErrorMessage(errorMessage);
		boolean success = false;
		if (ConfigUtils.fix) {
			Fixer fixer = new Fixer();
			LOGGER.info("fix " + ConfigUtils.fix);
			LOGGER.info("success " + success);
			if(asProfile != null)
			{
				LOGGER.info("asProfile " + asProfile);
				LOGGER.info("asProfile alternateNumber:  " + asProfile.getAlternateNumber());
				LOGGER.info("clusterId " + clusterId);
				success = fixer.fixPublicIds(asProfile, clusterId, true);
				System.out.println("Success : " + success);
			}
		}
		if (success) {
			reValidate(userId, displayObj, asProfile, hssValidator, userHssProfile, isRestRequest);
		} else {// success if false, when login or auth OCI are failing, so we haven't tried for
			// fix. If other intermediate OCIs are failing. Its exception are handled their
			// itself. As in that case, revalidation will tell if fix was successful or not
			displayObj.setFixStatus(FixResult.FAILED_MANUAL.str());
			if (errorMessage.contains("tel"))
				displayObj.setFixStatus(FixResult.FAILED_INUSE.str());
			LOGGER.info(displayObj.getFixStatus() + " " + userId);
		}
		if (asProfile != null)
		checkIncompleteProvisioning(userId, displayObj, asProfile, hssValidator, userHssProfile);
	}

	public static void main(String[] args) throws OciException {
//		ConfigUtils.loadPropertiesFile();
//		HssSoapClient hssSoapClient = new HssSoapClient();
//		hssSoapClient.deleteHssProfile("testuser@vk666668.hvoip.dk");
//		ConfigUtils.loadPropertiesFile();
		FixController controller = new FixController();
//		DevHelper helper = new DevHelper();
//		List<events> eventList = helper.getFailureList("5231743", "fixertest2@vk666668.hvoip.dk");
//		for (events ev : eventList) {
//			System.out.println(ev.getDocUserId());
//			ev.setAsUrl("188.181.136.4:2208");
//		}
//		List<FailureReportObj> failObj = controller.validateAndFix(eventList);
//		for (FailureReportObj obj : failObj) {
//			System.out.println(obj.getFixStatus());
//		}

		controller.process(null, null, "khk9dst11.ip.tdk.dk", "laege1adam@vk666668.hvoip.dk");
	}
}
