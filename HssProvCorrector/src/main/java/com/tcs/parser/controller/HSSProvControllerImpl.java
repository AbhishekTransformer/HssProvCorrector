package com.tcs.parser.controller;

import com.tcs.as.model.UserProfile;
import com.tcs.corrector.service.FixController;
import com.tcs.corrector.service.Parser;
import com.tcs.corrector.service.Refiner;
import com.tcs.elastic.service.App;
import com.tcs.exceptions.BadRequestException;
import com.tcs.fixer.http.FixApiResponse;
import com.tcs.generate.utils.DisplayReportGenerator;
import com.tcs.generate.utils.FailureReportObj;
import com.tcs.generate.utils.MailGenerator;
import com.tcs.generate.utils.ReportGenerator;
import com.tcs.oci.execution.ExecuteUserGetRequest21sp1;
import com.tcs.oci.utils.OciException;
import com.tcs.parser.dto.events;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.parser.utils.DevHelper;
import com.tcs.parser.utils.NSLookup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HSSProvControllerImpl extends EmController {
	private static Logger countLogger = LogManager.getLogger("countLogger");
	private static Logger LOGGER = LogManager.getLogger(HSSProvControllerImpl.class);

	@Override
	public List<events> getFailureList() {
		Parser parser = new Parser();
		List<events> failEventList = parser.getFailureList();
		return failEventList;
	}

	@Override
	public List<events> failEventListRefinement(List<events> failEventList) {
		Refiner refinerObj = new Refiner();
		return refinerObj.failEventListRefinement(failEventList);
	}

	@Override
	public List<FailureReportObj> validateAndFix(List<events> failEventList) {
		FixController fixController = new FixController();
		LOGGER.info("Final list count before validate and fix : " + failEventList.size());
		try {
			List<FailureReportObj> displayList = fixController.validateAndFix(failEventList);
			checkSecondAttempt2(displayList);
			

		}
		catch(Exception e)
		{
			LOGGER.info("After Completing Validate and Fix method, displayList"+ displayList.toString());
		}
		finally 
		{
			initiateMail(displayList);
			return displayList;
		}
	}


	public void checkSecondAttempt2(List<FailureReportObj> displayList) {
		try {
			Thread.sleep(ConfigUtils.revalidatingWaitingTime);
		} catch (InterruptedException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}
		LOGGER.info("Checking second attempt......");
		LOGGER.info("Total number of failed users second attempt : "+ displayList.size());
		FixController fixController = new FixController();
		List<events> failedEventList = new ArrayList<events>();
		try {
			for(int i=0;i<displayList.size();i++) {
				if(displayList.get(i).getFixStatus().contains("Manual Intervention")) {
					displayList.get(i).setAttemptCount("Second");
					failedEventList.add(displayList.get(i).getEvents());
				}
			}
			if(failedEventList.size()>0) {
				List<FailureReportObj> tempFixedList = fixController.validateAndFix(failedEventList);
				LOGGER.info("Size of temp displayList for reattempt method : " + tempFixedList.size());
				for(FailureReportObj obj:tempFixedList) {
					for(FailureReportObj displayObj: displayList) {
						try {
							if(obj.getEvents().getDocUserId()!=null && displayObj.getEvents().getDocUserId()!=null && obj.getEvents().getDocUserId().equalsIgnoreCase(displayObj.getEvents().getDocUserId())) {
								displayObj.setFixStatus(obj.getFixStatus());
								LOGGER.info("This user's fix status was updated after second attempt : " + obj.getEvents().getDocUserId());
							}
						}catch(Exception e) {
							LOGGER.error(e);
						}

					}
				}
			}
			LOGGER.info("Total number of failed users second attempt last of method : "+ displayList.size());
		}
		catch(Exception e) {
			LOGGER.error("Exception in reattampt method ");
			LOGGER.error(e);
			e.printStackTrace();
		}
	}

	public void checkSecondAttempt1(List<FailureReportObj> displayList){
		LOGGER.info("Checking second attempt......");
		LOGGER.info("Total number of failed users second attempt : "+ displayList.size());
		try {
			Thread.sleep(ConfigUtils.revalidatingWaitingTime);
		} catch (InterruptedException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}
		FixController fixController = new FixController();
		List<events> tempEventList = new ArrayList<events>();
		try {
			for(FailureReportObj failedObj : displayList) {
				if(failedObj.getFixStatus().contains("Manual Intervention") && failedObj.getAttemptCount().equalsIgnoreCase("first")) {
					tempEventList.add(failedObj.getEvents());
				}
			}
			for(events failedObj : tempEventList) {
				System.out.println("================================");
				System.out.println(failedObj);
				System.out.println(failedObj.getAsClusterId());
			}
			List<FailureReportObj> tempDisplayList= fixController.validateAndFix(tempEventList);
			if(tempDisplayList.size()>0) {
				List<FailureReportObj> displayList2= new ArrayList<FailureReportObj>();
				for(FailureReportObj tempObj: tempDisplayList) {
					for(FailureReportObj tempDisplayObj : displayList) {
						try {
							if(tempObj.getEvents().getDocUserId().equalsIgnoreCase(tempDisplayObj.getEvents().getDocUserId())) {
								tempObj.setAttemptCount("Second");
								displayList2.add(tempObj);
								LOGGER.info("User added after second attempt : " + tempObj.getEvents().getDocUserId());
							}
						}
						catch(Exception e) {
							LOGGER.error("Error : " + e);
						}
					}
				}
				displayList.clear();
				displayList.addAll(displayList2);
			}
		}
		catch(Exception e) {
			LOGGER.error("Exception in checking of second attempts : "+e);
			e.printStackTrace();
		}
	}

	public FixApiResponse validateAndFix(String userId, String source) throws BadRequestException {
		NSLookup ns = new NSLookup();
		String clusterId = ns.getAS(userId);
		FixApiResponse response = new FixApiResponse();
		FixController fixController = new FixController();
		LOGGER.info("ClusterId :" + clusterId);
		if(clusterId == null)
		{
			LOGGER.info("ClusterId :" + clusterId);
			LOGGER.info("UserId doesn't exist in AS");
//			response.setUserId(userId);
//			response.setClusterId(clusterId);
		}
		//String clusterId="khk9dst11.ip.tdk.dk";
//		FixController fixController = new FixController();
		LOGGER.info("ClusterId :" + clusterId);
		List<FailureReportObj> displayList = fixController.validateAndFix(clusterId, userId);
		//checkSecondAttempt2(displayList);

//		FixApiResponse response = new FixApiResponse();
		response.setUserId(userId);
		response.setClusterId(clusterId);
		if (null != displayList && !(displayList.isEmpty())) {
			FailureReportObj displayObj = displayList.get(0);
			response.setFixStatus(displayObj.getFixStatus());
			response.setReason(displayObj.getFailureReason() + " " + displayObj.getErrorMessage());

			HashMap<String,Object> indexData=new HashMap<String,Object>();
			indexData.put("userId",userId);
			indexData.put("failureReason", displayObj.getFailureReason());
			indexData.put("errorMessage", displayObj.getErrorMessage());
			indexData.put("clusterId", clusterId);
			indexData.put("status", displayObj.getFixStatus());
			indexData.put("source", source);
			indexData.put("fixAttempt", "First");
			UserProfile userProfile = null;
			try {
				if(clusterId != null)
				userProfile = new ExecuteUserGetRequest21sp1(clusterId).runExecution(userId);
			} catch (OciException e) {
				LOGGER.info("Error in getting user profile. Can't update groupId, serviceProviderId for this request");
				e.printStackTrace();
			}
			if (null != userProfile) {
				indexData.put("serviceProvider", userProfile.getServiceProviderId());
				indexData.put("groupId", userProfile.getGroupId());
			}
			try {
				pushElasticData(indexData, null);
			}
			catch(Exception e)
			{
				LOGGER.info("Error while Pushing Data to Elastic "+e.getMessage());
			}
			return response;
		}
		response.setFixStatus("Fix Success");
		response.setReason("No Error Found");
		return response;
	}

	private void initiateMail(List<FailureReportObj> displayList) {
		if (null != displayList && displayList.size() != 0) {
			ReportGenerator<FailureReportObj> failEventReport = new DisplayReportGenerator();
			String excelName = "ConsideredList" + ConfigUtils.getCurrentDateWithTimeStamp() + ".xls"; /// added
			failEventReport.generateReport(displayList, ConfigUtils.excelFilePath + excelName);
			MailGenerator.sendMail(true, ConfigUtils.mailSubject, ConfigUtils.excelFilePath + excelName);
		} else {
			MailGenerator.sendMail(false, ConfigUtils.mailSubject, ConfigUtils.excelFilePath + "ConsideredList" + ConfigUtils.getCurrentDate() + ".xls");
		}
	}

	@Override
	public void pushElasticData(HashMap<String, Object> indexData, List<FailureReportObj> failureReport) {
		//HashMap<String, Object> indexData = new HashMap<String, Object>();
		/*int successCount=0; int failureCount=0;
		for(FailureReportObj failureObject : failureReport) {
			if(failureObject.getFixStatus().equalsIgnoreCase("success"))
				successCount++;
			else
				failureCount++;
		}
		indexData.put("Total Count", (successCount+failureCount));
		indexData.put("Success Count", successCount);
		indexData.put("Failure Count",failureCount);
		System.out.println("Total Count" +  (successCount+failureCount));
		System.out.println("Success Count" + successCount);
		System.out.println("Failure Count" + failureCount);
		*/
		try {
			App.app(indexData , failureReport);
		} catch (IOException e) {
			LOGGER.info("Under catch: "+e.getMessage());
			e.printStackTrace();
		} catch (ParseException e) {
			LOGGER.info("Under catch: "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		ConfigUtils.loadPropertiesFile();
		HSSProvControllerImpl controller = new HSSProvControllerImpl();
		DevHelper helper = new DevHelper();
		List<events> eventList = helper.getFailureList("5272355", "fixertest6@vk666668.hvoip.dk");

		List<FailureReportObj> failObj = controller.validateAndFix(eventList);
		for (FailureReportObj obj : failObj) {
			System.out.println(obj.getFixStatus());
		}
	}

	@Override
	public List<FailureReportObj> checkSecondAttempt(List<events> failEventList) {
		// TODO Auto-generated method stub
		return null;
	}
}
