package com.tcs.generate.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class EventIdReportGenerator extends ReportGenerator<Integer>{
	public void writeBook(Integer displayObj, Row row) {
	    Cell cell = row.createCell(0);
	    cell.setCellValue(displayObj);
	}
	public void createColumnHeaders(Row row){
		Cell cell = row.createCell(0);
	    cell.setCellValue("EventId");
	}
}
