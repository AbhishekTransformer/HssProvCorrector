package com.tcs.generate.utils;

import com.tcs.parser.dto.events;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class EventReportGenerator extends ReportGenerator<events>{
		
		public void writeBook(events displayObj, Row row) {
		    Cell cell = row.createCell(0);
		    cell.setCellValue(displayObj.getEventId());
		 
		    cell = row.createCell(1);
		    cell.setCellValue(displayObj.getDocUserId());
		 
		    cell = row.createCell(2);
		    cell.setCellValue(displayObj.getDocServiceProviderId());
		    
		    cell = row.createCell(3);
		    cell.setCellValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(displayObj.getTimeEndProcess().toString())), 
	                TimeZone.getDefault().toZoneId()).toString());
		    
		    cell = row.createCell(4);
		    cell.setCellValue(displayObj.getDocGroupId());
		    
		    cell = row.createCell(5);
		    cell.setCellValue(displayObj.getDocType());
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
		}
		
}
