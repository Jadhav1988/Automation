package com.QACOE.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataConfig {

	static CommonSettings set = new CommonSettings();
	private static XSSFSheet ExcelWSheet;

	private static XSSFWorkbook ExcelWBook;

	private static XSSFCell Cell;

	private static XSSFRow Row;

	// This method is to set the File path and to open the Excel file, Pass
	// Excel Path and
	// Sheetname as Arguments to this method

	public static void openExcelFile(String FileName, String SheetName)
			throws Exception {

		try {

			// Open the Excel file

			FileInputStream ExcelFile = new FileInputStream(
					(System.getProperty("user.dir") + "\\inputs\\" + FileName + ".xlsx"));

			// Access the required test data sheet

			ExcelWBook = new XSSFWorkbook(ExcelFile);

			ExcelWSheet = ExcelWBook.getSheet(SheetName);

		} catch (Exception e) {

			throw (e);

		}

	}

	public static XSSFSheet getSheet() {
		return ExcelWSheet;
	}

	// This method is to read the test data from the Excel cell,
	// in this we are passing parameters as Row num and Col num

	public static String getCellData(int RowNum, int ColNum) throws Exception {

		try {

			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			String CellData = Cell.getRawValue();
			switch (Cell.getCellType()) {
			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
				CellData = String.valueOf(Cell.getRawValue());
				set.debugLogging(
						"Read from excel: " + Cell.getNumericCellValue(),
						"Info");
				break;
			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
				CellData = Cell.getStringCellValue();
				set.debugLogging(
						"Read from excel: " + Cell.getStringCellValue(), "Info");
				break;
			}
			return CellData;

		} catch (Exception e) {

			return "";

		}

	}

	public static String getnumCellData(int RowNum, int ColNum)
			throws Exception {

		try {

			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			String CellData = Cell.getRawValue();
			switch (Cell.getCellType()) {
			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
				CellData = String.valueOf(Cell.getRawValue());
				set.debugLogging(
						"Read from excel: " + Cell.getNumericCellValue(),
						"Info");
				break;
			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
				CellData = Cell.getStringCellValue();
				set.debugLogging(
						"Read from excel: " + Cell.getStringCellValue(), "Info");
				break;
			}

			// String CellData = Cell.getRawValue();

			return CellData;

		} catch (Exception e) {

			return "";

		}

	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters

	public static void setCellData(String Result, int RowNum, int ColNum)
			throws Exception {

		try {

			Row = ExcelWSheet.getRow(RowNum);

			Cell = Row.getCell(ColNum,
					org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);

			if (Cell == null) {

				Cell = Row.createCell(ColNum);

				Cell.setCellValue(Result);

			} else {

				Cell.setCellValue(Result);

			}

			// Constant variables Test Data path and Test Data file name

			FileOutputStream fileOut = new FileOutputStream(
					"C:\\Users\\Mohan.jadhav\\Desktop\\output" + "Sheet1.xlsx");

			ExcelWBook.write(fileOut);

			fileOut.flush();

			fileOut.close();

		} catch (Exception e) {

			throw (e);

		}

	}

}
