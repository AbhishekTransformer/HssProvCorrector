package com.tcs.corrector.service;

import com.tcs.as.model.AlternateNumberEntry;
import com.tcs.as.model.AsProfile;
import com.tcs.as.model.SCAEndpoint;
import com.tcs.hss.client.HssProfile;
import com.tcs.hss.client.PrivateProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HssValidator {
	private static Logger LOGGER = LogManager.getLogger(HssValidator.class);

	//check if this userId has HSS profile created/corrected, after this failure
	public ErrorReporter checkpublicIdsExists(String userId, AsProfile asProfile, HssProfile userHssProfile, List<String> imsiValues, List<String> publicIdsFromDNS) {
		ErrorReporter errorFinder = new ErrorReporter();
		LOGGER.info("HssValidator:userHssProfile " + userHssProfile);
		LOGGER.info("HssValidator:imsiValues " + imsiValues);
		LOGGER.info("HssValidator:publicIdsFromDNS " + publicIdsFromDNS);
		if (null != asProfile) {
			if (null != userHssProfile) {
				List<String> asPublicIds = getAsPublicIds(asProfile);
				LOGGER.info("HssValidator:userHssProfile.getPrivateIdList() " + userHssProfile.getPrivateIdList());
				LOGGER.info("HssValidator:userHssProfile.getPublicIdList() " + userHssProfile.getPublicIdList());
				List<String> hssPublicIds = userHssProfile.getPublicIdList();
				LOGGER.info("HssValidator:hssPublicIds " + hssPublicIds);
				LOGGER.info("HssValidator:asPublicIds " + asPublicIds);
				List<PrivateProfile> hssPrivateIds = userHssProfile.getPrivateIdList();
				if (null != asPublicIds && !(asPublicIds.isEmpty())) {
					if (null != hssPublicIds && !(hssPublicIds.isEmpty())) {
						publicIdComparison(errorFinder, asPublicIds, hssPublicIds, imsiValues, publicIdsFromDNS, userId);
					} else {
						LOGGER.info("No HSS public id found " + userId);
						String temp = " Expected =";
						for (String asPubId : asPublicIds) {
							temp = temp + ", " + asPubId;
						}
						errorFinder.setError(ErrorReporter.HSSZEROPUBLICIDS, "Expected = ", asPublicIds);

					}
				} else if (null != hssPrivateIds) {
					List<PrivateProfile> hssPrivateIdsSameAsASProfile = hssPrivateIds.stream().filter(e -> e.getPrivateUserId().equals(userId) || e.getPrivateUserId().equals(asProfile.getProfile().getUserId())).collect(Collectors.toList());
					List<String> msisdns = hssPrivateIdsSameAsASProfile.stream().map(PrivateProfile::getMsisdn).collect(Collectors.toList());
					if (!hssPrivateIdsSameAsASProfile.isEmpty())
						errorFinder.setError(ErrorReporter.INCORRECTMSISDN, "MSISDN found for private user id same as BW user id; Check if corrupted", msisdns); // Implemented based on technician's request, error reported by technicians for user id starting with 900000
				} else {
					LOGGER.info("No AS public id found " + userId);
					//Changing this for Technician Tool APi responses only
					List<String> filteredHssIds = hssPublicIds.stream().filter(pubId -> !("sip:" + userId).equals(pubId)).collect(Collectors.toList());
					if (null != filteredHssIds && filteredHssIds.size() != 0) {
						String asMissingList = "Extra Public ids : ";
						for (String pubId : filteredHssIds) {
							LOGGER.info("But public ids found on Hss i.e AS Profile missing this public Id :" + pubId);   // most of the times should be something like 238010157442707@ims.mnc001.mcc238.3gppnetwork.org. Check what to do for this
							asMissingList = asMissingList + " , " + pubId;
						}
						errorFinder.setError(ErrorReporter.ASZEROPUBLICIDS, "Expected = ", filteredHssIds);
					} else {
						LOGGER.info("No PublicId on As and HSS");
					}
					//pubIdErrorList.add("ASMissing "+"");
					//2 scenarios here : one is if no publicid value on AS then also it should be wrong?
					//second is if user is deleted so there is no record , in that case we don't have to consider for fix, but if uncomment, it is considering it for fix
					//third, if user is deleted, need to check in HSS profile that is should also be not here.
				}

			}else{
				LOGGER.info("Problem in getting Hss profile for user "+userId); //send null from cTCH BLOCKS, AFTER THROWING ALL USER DEFINED EXCPEITONS FORM INSIDE. SO THAT IN CASE of our intermediate erorrs, we know it and requires manual intervention, instead of going for a fix considering it a failure
				// mUst be considered for fix? as AS profile is there, but Hss profile is not there (to confirm this change the code, as for fault reason also a new object is being created for userHss Profile). But check inside code, as this will be there is any error in parsing response ?
			}
		}else{
			LOGGER.info("Problem in getting AS profile for user or AS profile does not exist "+userId); //send null from cTCH BLOCKS, AFTER THROWING ALL USER DEFINED EXCPEITONS FORM INSIDE. SO THAT IN CASE of our intermediate erorrs, we know it and requires manual intervention, instead of going for a fix considering it a failure
			List<String> hssPublicIds = userHssProfile.getPublicIdList();
			if(null!=hssPublicIds && !(hssPublicIds.isEmpty())){
				LOGGER.info("AS profile is not there, but still public ids found on vHSS");  // check if need to consiider it in a fix
			}
		}
		return errorFinder;
		/*if(pubIdErrorList.size()!=0){
			for (String entry : pubIdErrorList) {
			    if(entry.contains("HSSMissing"))
			    	return false;
			    else
			    	return true;
			}
		}*/


		//else
		//return false;
		// check this , as if due to internal error there is no public ids, then we do not want to fix it, it requries manual intervention.
		// for that, do exception handling , throw error, and put manual intervention for that
		// and if genuinely there is no public Id, then go for a fix.


		//return true;
	}
	private void publicIdComparison(ErrorReporter errorFinder, List<String> asPublicIds,
			List<String> hssPublicIds, List<String> imsiValues, List<String> publicIdsFromDNS, String userId) {

		LOGGER.info("Starting Comparison");
		LOGGER.info("AS Public Ids : "+asPublicIds);
		LOGGER.info("HSS Public Ids : "+hssPublicIds);
		LOGGER.info("Imsi Values : "+imsiValues);
		LOGGER.info("DNS public Ids : "+publicIdsFromDNS);

		if(publicIdsFromDNS != null && !publicIdsFromDNS.isEmpty()) {

			//Comparing only DNS values here
			List<String> failDnsPubIds = new ArrayList<>(publicIdsFromDNS);
			List<String> dnsMissingOnAS = new ArrayList<>();
			List<String> dnsMissingOnHSS = new ArrayList<>();
			List<String> dnsMissingOnBoth = new ArrayList<>();

			for(String publicIdFromDNS:publicIdsFromDNS) {

				if ((!asPublicIds.contains(publicIdFromDNS)) && (!hssPublicIds.contains(publicIdFromDNS))) {
					LOGGER.info("DNS public record not found on both AS and HSS : " + publicIdFromDNS);
					dnsMissingOnBoth.add(publicIdFromDNS);
				} else if (!asPublicIds.contains(publicIdFromDNS)) {
					LOGGER.info("DNS public record not found on AS : " + publicIdFromDNS);
					dnsMissingOnAS.add(publicIdFromDNS);
				} else if (!hssPublicIds.contains(publicIdFromDNS)) {
					LOGGER.info("DNS public record not found on HSS : " + publicIdFromDNS);
					dnsMissingOnHSS.add(publicIdFromDNS);
				} else {
					LOGGER.info("DNS public record found on both AS and HSS : " + publicIdFromDNS);
					failDnsPubIds.remove(publicIdFromDNS);
				}
			}

			if (!dnsMissingOnAS.isEmpty())
				errorFinder.setError(ErrorReporter.DNSERROR, "DNS record not found on AS : ", dnsMissingOnAS);
			if (!dnsMissingOnHSS.isEmpty())
				errorFinder.setError(ErrorReporter.DNSERROR, "DNS record not found on HSS : ", dnsMissingOnHSS);
			if (!dnsMissingOnBoth.isEmpty())
				errorFinder.setError(ErrorReporter.DNSERROR, "DNS record not found on both AS and HSS : ", dnsMissingOnBoth);

		}
		publicIdComparison(errorFinder, asPublicIds, hssPublicIds, imsiValues, userId);

	}
	private void publicIdComparison(ErrorReporter errorFinder, List<String> asPublicIds,
			List<String> hssPublicIds, List<String> imsiValues, String userId) {
		List<String> tempIMSIIds = new ArrayList<>(imsiValues);
		List<String> tempHssIds = new ArrayList<>(hssPublicIds);
		List<String> extraImsiIDs = new ArrayList<>();
		if(imsiValues != null && !imsiValues.isEmpty()) {
			String extraIMSIList = "Extra IMSI public ids : ";
			boolean isHSSExtraIMSI=false;

			LOGGER.info("imsiList : "+imsiValues);

			for (String hssPublicId : hssPublicIds){
				if(hssPublicId.contains("@") && hssPublicId.contains("3gppnetwork")) {
					String imsiFromHSS = hssPublicId.substring(
							hssPublicId.startsWith("sip:")? "sip:".length() : 0, hssPublicId.indexOf("@"));
					LOGGER.info("imsiHss : "+imsiFromHSS);
					if (imsiValues.contains(imsiFromHSS)) {
						LOGGER.info("IMSI found on both VASHLR and HSS :" + imsiFromHSS);
						tempHssIds.remove(hssPublicId);
						tempIMSIIds.remove(imsiFromHSS);
					} else {
						LOGGER.info("Hss Profile having extra IMSI Id : " + hssPublicId);
						extraIMSIList = extraIMSIList + " , " + hssPublicId;
						extraImsiIDs.add(hssPublicId);
						isHSSExtraIMSI = true;
					}
				}
			}

			if(isHSSExtraIMSI) {
				errorFinder.setError(ErrorReporter.HSSEXTRAIMSI, "Extra IMSI public ids : ", extraImsiIDs);
			}


			if(null!=tempIMSIIds && !tempIMSIIds.isEmpty()){
				String hssMissingIMSIList = "HSS missing IMSI ids : ";
				for (String imsi : tempIMSIIds) {
					LOGGER.info("HSS Profile missing this IMSI Id :" + imsi);
					hssMissingIMSIList = hssMissingIMSIList + " , " + imsi;
				}
				errorFinder.setError(ErrorReporter.HSSMISSINGIMSI, "HSS missing IMSI ids : ", tempIMSIIds);
			}
		}
		publicIdComparison(errorFinder, asPublicIds, tempHssIds, userId);
	}

	//one public id on HSS is for <imsiNumber>@3g99network. To compare that need to fire GET on IMSI - TODO
	private void publicIdComparison(ErrorReporter errorFinder, List<String> asPublicIds, List<String> hssPublicIds, String userId) {
		List<String> tempAsPubIds = hssPublicIds;
		List<String> hssMissingList = new ArrayList<>();

		for (String asPubId : asPublicIds) {
			if (hssPublicIds.contains(asPubId)) {
				LOGGER.info("Public Id found on both As and HSS " + asPubId);
				tempAsPubIds.remove(asPubId);
			} else {
				LOGGER.info("Hss Profile missing this public Id :" + asPubId);
				hssMissingList.add(asPubId);
			}
		}
		if (!hssMissingList.isEmpty()) {
			errorFinder.setError(ErrorReporter.HSSMISSING, "Hss Missing public ids : ", hssMissingList);
		}
		//Changing this for Technician Tool APi responses
		if (null != tempAsPubIds && tempAsPubIds.size() != 0) {
			List<String> asMissingList = tempAsPubIds.stream().filter(pubId -> !("sip:" + userId).equals(pubId)).collect(Collectors.toList());
			if (!asMissingList.isEmpty()) {
				errorFinder.setError(ErrorReporter.ASMISSING, "Hss Extra public ids : ", asMissingList);
			}
		}
	}

	private List<String> getAsPublicIds (AsProfile asProfile){
		List<String> asPublicIdsList = new ArrayList<String> ();
		/*if(null!=asProfile.getProfile().getUserId() && !(asProfile.getProfile().getUserId().trim().isEmpty()))
			asPublicIdsList.add("sip:"+asProfile.getProfile().getUserId());*/
		if(null != asProfile.getProfile().getPhoneNumber() && !(asProfile.getProfile().getPhoneNumber().trim().isEmpty()))
			asPublicIdsList.add("tel:+45"+asProfile.getProfile().getPhoneNumber());
		if(null != asProfile.getProfile().getAccessDeviceEndpoint().getLinePort() && !(asProfile.getProfile().getAccessDeviceEndpoint().getLinePort().trim().isEmpty()) )
			asPublicIdsList.add("sip:"+asProfile.getProfile().getAccessDeviceEndpoint().getLinePort());
		List<String> sipAliasList = asProfile.getProfile().getSIPAliasList();
		if(null!=sipAliasList && !(sipAliasList.isEmpty())){
			for (String sipAlias : sipAliasList){
				asPublicIdsList.add("sip:"+sipAlias);
			}
		}
		List<AlternateNumberEntry> altNumList = asProfile.getAlternateNumber().getAlternateEntry();
		if(null!=altNumList && !(altNumList.isEmpty())){
			for (AlternateNumberEntry altNumEntry : altNumList){
				if( null != altNumEntry.getPhoneNumber() && !(altNumEntry.getPhoneNumber().trim().isEmpty()))
					asPublicIdsList.add("tel:+45"+altNumEntry.getPhoneNumber());
			}
		}
		List <Map<String,String>> scaEndPointList = asProfile.getScaProfile().getEndpointTable();
		if(null!=scaEndPointList && !(scaEndPointList.isEmpty())){
			for (Map<String,String> endPoint: scaEndPointList){
				if(null != endPoint.get(SCAEndpoint.LINE_PORT.str()) && !(endPoint.get(SCAEndpoint.LINE_PORT.str()).trim().isEmpty())){
					if(null!=asProfile.getProfile().getUserId() && !(asProfile.getProfile().getUserId().trim().isEmpty()))
					{
						if (!(endPoint.get(SCAEndpoint.LINE_PORT.str()).equals(asProfile.getProfile().getUserId()))) {
							asPublicIdsList.add("sip:" + endPoint.get(SCAEndpoint.LINE_PORT.str()));
						}
					} else
						asPublicIdsList.add("sip:" + endPoint.get(SCAEndpoint.LINE_PORT.str()));
				}
			}
		}
		return asPublicIdsList;
	}


	public static void main(String[] args) {
		List<String> asPubIds = Arrays.asList("");
		List<String> hssPubIds = Arrays.asList("sip:238010161817967@ims.mnc001.mcc238.3gppnetwork.org");
		List<String> imsiPubIds = Arrays.asList("238010161817967");
		List<String> dnsPubIds = Arrays.asList("");
		HssValidator validator = new HssValidator();
//		validator.publicIdComparison(new HashMap<>(), asPubIds, hssPubIds, imsiPubIds,dnsPubIds,"");
	}
}
