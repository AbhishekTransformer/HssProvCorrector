package com.tcs.generate.utils;

import com.tcs.parser.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public abstract class ReportGenerator<T> {
	private static Logger LOGGER = LogManager.getLogger(ReportGenerator.class);

	public void generateReport(List<T> displayList, String filePath) {
		try {
			writeExcel(displayList, filePath /*ConfigUtils.excelFilePath*/);
		} catch (IOException e) {
			LOGGER.error(ConfigUtils.getStackTraceString(e));
		}
	}

	private void writeExcel(List<T> displayList, String excelFilePath) throws IOException {
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		int rowCount = 0;
		Row row = sheet.createRow(rowCount);
		createColumnHeaders(row);

		for (T displayObj : displayList) {
			row = sheet.createRow(++rowCount);
			try {
				writeBook(displayObj, row);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
			workbook.write(outputStream);
		}
		workbook.close();
	}
	public abstract void writeBook(T displayObj, Row row);
	public abstract void createColumnHeaders(Row row);
}
