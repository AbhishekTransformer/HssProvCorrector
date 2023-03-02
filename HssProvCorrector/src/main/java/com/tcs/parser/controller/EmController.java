package com.tcs.parser.controller;

import com.tcs.generate.utils.FailureReportObj;
import com.tcs.generate.utils.MailGenerator;
import com.tcs.parser.dto.events;
import com.tcs.parser.utils.ConfigUtils;
import com.tcs.service.dbconnectionmanager.EmDBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class EmController {
	private static Logger LOGGER = LogManager.getLogger(EmController.class);
	private List<events> failEventList = new ArrayList<events>();
	protected HashMap<String, Object> indexData = new HashMap<String, Object>();
	List<FailureReportObj> displayList = new ArrayList<FailureReportObj>();

	public void process() {
		try {
			failEventList = getFailureList();
			//indexData.put("Failed Events",failEventList.size());
			if (null != failEventList && failEventList.size() != 0) {
				failEventList = failEventListRefinement(failEventList);
				if (null != failEventList && failEventList.size() != 0) {
					displayList = validateAndFix(failEventList);
				}
			} else {
				LOGGER.info("No fail Event List found ");
				MailGenerator.sendMail(false, ConfigUtils.mailSubject,
						ConfigUtils.excelFilePath + "ConsideredList" + ConfigUtils.getCurrentDate() + ".xls");
			}
			pushElasticData(indexData, displayList);
		} catch (Exception e) {
			LOGGER.info(ConfigUtils.getStackTraceString(e));
		} finally {
			EmDBConnection.closeSessionFactory();
		}
		ConfigUtils.storeIndexList.clear();
		LOGGER.info("Store Index List cleared in process method ="+ConfigUtils.storeIndexList);
	}

	public abstract List<events> failEventListRefinement(List<events> failEventList);

	public abstract List<events> getFailureList();

	public abstract List<FailureReportObj> validateAndFix(List<events> failEventList);

	public abstract List<FailureReportObj> checkSecondAttempt(List<events> failEventList);

	public abstract void pushElasticData(HashMap<String, Object> indexData, List<FailureReportObj> failEventList);
}
