package com.tcs.corrector.service;

import com.tcs.generate.utils.EventIdReportGenerator;
import com.tcs.generate.utils.EventReportGenerator;
import com.tcs.generate.utils.ReportGenerator;
import com.tcs.parser.dao.EventsDao;
import com.tcs.parser.dao.HSSEventsDao;
import com.tcs.parser.dao.ProcessLogDao;
import com.tcs.parser.dto.events;
import com.tcs.parser.dto.processLog;
import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private static Logger LOGGER = LogManager.getLogger(Parser.class);

    public List<events> getFailureList() {
        List<Integer> subEventsFailIdsList = parseSubProcesses();
        List<events> failEventList = parseProcesses(subEventsFailIdsList);
        ReportGenerator<events> beforeRefinementfailEventReport = new EventReportGenerator();
        beforeRefinementfailEventReport.generateReport(failEventList, ConfigUtils.excelFilePath + "beforeRefineError" + ConfigUtils.getCurrentDate() + ".xls");
        //MailGenerator.sendMail(true,"Before Refinement Errors",ConfigUtils.excelFilePath+"beforeRefineError.xls");
        return failEventList;
    }

    private List<Integer> parseSubProcesses() {
        ProcessLogDao procesLogdao = new ProcessLogDao();
        List<processLog> subEventsFailureList = procesLogdao.fetchSubProcessData();
        List<Integer> subEventsFailIdsList = new ArrayList<Integer>();
        /*To remove duplicate eventIds*/
        HashMap<Integer, processLog> failEventMap = new HashMap<Integer, processLog>();
        for (processLog event : subEventsFailureList) {
            if (!failEventMap.containsKey(event.getEventId())) {
                failEventMap.put(event.getEventId(), event);
                subEventsFailIdsList.add(event.getEventId()); //update these both list after fetching from events table
            }//can try directly to fetch only eventIds from the above query, instead of all the data, if failure reasons are not required
        }
        subEventsFailureList = new ArrayList<processLog>(failEventMap.values());  // can use failure list for listing failure reasons in future
        for (processLog evt : subEventsFailureList) {
            LOGGER.info("SubProcess Failed event :" + evt.getEventId());
        }
        return subEventsFailIdsList;
    }

    private List<events> parseProcesses(List<Integer> subEventsFailIdsList) {

        EventsDao eventsDao = new HSSEventsDao();
        List<events> failEventList = new ArrayList<events>();

        if (null != subEventsFailIdsList && subEventsFailIdsList.size() != 0) {
            failEventList = eventsDao.getEventDetails(subEventsFailIdsList, ConfigUtils.getYesterdayTimeStamp()); // THis ia a fail list in 24 hours
            //of mentioned oci, scale users with create and set vhss errors

            List<Integer> failIdsList = new ArrayList<Integer>();
            if (null != failEventList && failEventList.size() != 0) {
                for (events evt : failEventList) {
                    LOGGER.info("Failed event considered for fix before refinement: " + evt.getEventId() + "userId : " + evt.getDocUserId());
                    failIdsList.add(evt.getEventId());
                }
                LOGGER.info("Final considered failures Before Refinement: " + failEventList.size() + ", Failures found at sub-process level : " + subEventsFailIdsList.size());
            }
            otherErrorForConsideredOcis(eventsDao, subEventsFailIdsList); //To be removed
            vHSSErrorForOtherOcis(failIdsList, subEventsFailIdsList); // To be removed
        } else {
            otherErrorForConsideredOcis(eventsDao, subEventsFailIdsList); //To be removed
        }
		return failEventList;
		//indexData.put("Total Service requests",new Integer(eventsList.size())); Not finding total requests as they will be too many
	}

	private void vHSSErrorForOtherOcis(List<Integer> failIdsList, List<Integer> subEventsFailIdsList) {
		subEventsFailIdsList.removeAll(failIdsList);
		if(null!=subEventsFailIdsList &&  subEventsFailIdsList.size()!=0) //Remove this later
        {
            for (Integer evtId : subEventsFailIdsList) {
                LOGGER.info("(Create/Set vHSS failure) Failed event skipped from fix (for other OCis or non-scale users): To be checked for consideration :" + evtId);
            }
            ReportGenerator<Integer> otherCreateSetHssfailEventReport = new EventIdReportGenerator();
            otherCreateSetHssfailEventReport.generateReport(subEventsFailIdsList, ConfigUtils.excelFilePath + "othervHSSError" + ConfigUtils.getCurrentDate() + ".xls");
            //MailGenerator.sendMail(true,"Create/Set vHSS errors of other OCIs",ConfigUtils.excelFilePath+"othervHSSError.xls");
        }
    }

    private void otherErrorForConsideredOcis(EventsDao eventsDao, List<Integer> subEventsFailIdsList) {
        List<events> otherFailEventList = eventsDao.fetchOtherProcessData(subEventsFailIdsList, ConfigUtils.getYesterdayTimeStamp());//These are other events
        //of mentioned oci, scale users in 24 hours, which were not found in above error.
        LOGGER.info("Other failures size: " + otherFailEventList.size());
        if (null != otherFailEventList && otherFailEventList.size() != 0) {
            for (events evt : otherFailEventList) {
                LOGGER.info("Other Failed event for mentioned ocis (not with create/set vHSS failure): To be checked for consideration :" + evt.getEventId());
            }
            ReportGenerator<events> otherfailEventReport = new EventReportGenerator();
            otherfailEventReport.generateReport(otherFailEventList, ConfigUtils.excelFilePath + "OtherErrors" + ConfigUtils.getCurrentDate() + ".xls");
            //MailGenerator.sendMail(true,"Other considerable errors for mentioned OCIs",ConfigUtils.excelFilePath+"OtherErrors.xls");
        }
    }

}
