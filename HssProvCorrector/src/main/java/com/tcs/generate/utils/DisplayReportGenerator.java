package com.tcs.generate.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class DisplayReportGenerator extends ReportGenerator<FailureReportObj>{
	public void writeBook(FailureReportObj displayObj, Row row) {
		try {
			Cell cell = row.createCell(0);
			cell.setCellValue(displayObj.getEvents().getEventId());
		 
		    cell = row.createCell(1);
		    cell.setCellValue(displayObj.getEvents().getDocUserId());
		 
		    cell = row.createCell(2);
		    cell.setCellValue(displayObj.getEvents().getDocServiceProviderId());
		    
		    cell = row.createCell(3);
		    cell.setCellValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(displayObj.getEvents().getTimeEndProcess().toString())), 
	                TimeZone.getDefault().toZoneId()).toString());
		    
		    cell = row.createCell(4);
		    cell.setCellValue(displayObj.getEvents().getDocGroupId());
		    
		    cell = row.createCell(5);
		    cell.setCellValue(displayObj.getEvents().getDocType());
		    
		    cell = row.createCell(6);
		    cell.setCellValue(displayObj.getFailureReason());
		    
		    cell = row.createCell(7);
		    cell.setCellValue(displayObj.getErrorMessage());
		    
		    cell = row.createCell(8);
		    cell.setCellValue(displayObj.getFixStatus());
		    
		    cell = row.createCell(9);
		    cell.setCellValue(displayObj.getAttemptCount());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void createColumnHeaders(Row row){
		Cell cell = row.createCell(0);
	    cell.setCellValue("Fail EventId");
	 
	    cell = row.createCell(1);
	    cell.setCellValue("UserId");
	 
	    cell = row.createCell(2);
	    cell.setCellValue("ServiceProvider");
	    
	    cell = row.createCell(3);
	    cell.setCellValue("Error Time");
	    
	    cell = row.createCell(4);
	    cell.setCellValue("GroupId");
	    
	    cell = row.createCell(5);
	    cell.setCellValue("DocType");
	    
	    cell = row.createCell(6);
	    cell.setCellValue("Failure Reason");
	    
	    cell = row.createCell(7);
	    cell.setCellValue("Error Message");
	    
	    cell = row.createCell(8);
	    cell.setCellValue("Fix Status");
	    
	    cell = row.createCell(9);
	    cell.setCellValue("Fix attempt");
	}
	
}
