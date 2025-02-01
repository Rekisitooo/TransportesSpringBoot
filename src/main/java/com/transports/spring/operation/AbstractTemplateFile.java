package com.transports.spring.operation;


import org.apache.poi.ss.usermodel.*;

import java.util.Calendar;

public abstract class AbstractTemplateFile {

    protected static final int START_ROW_DAYS = 3;// Primera columna
    protected static final int START_COL_DAYS = 0;
    protected static final String CALENDAR_NAME_FILE = "calendar";
    protected static final String XSL_EXTENSION = ".xlxs";
    protected static final String SEPARATION_CHARACTER = "_";
    protected static final int MONDAYS_COL = 0;
    protected static final int SUNDAYS_COL = 6;
    protected static final int OUT_OF_DAYS_COLUM_SCOPE = 7;

    protected AbstractTemplateFile(){}

    protected static CellStyle getCellStyleForTransportCell(final Workbook newExcel, final Cell cell) {
        final CellStyle cellStyle = newExcel.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        final Font font = newExcel.createFont();
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        font.setBold(true);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        return cellStyle;
    }

    protected static Row createNewExcelRow(final Sheet sheet, final int currentRow) {
        Row row = sheet.getRow(currentRow);
        if (row == null) {
            row = sheet.createRow(currentRow);
        }
        return row;
    }

    protected static int getInitialDayOfTheMonthColumn(final Calendar templateMonthCalendar) {
        int currentCol = templateMonthCalendar.get(Calendar.DAY_OF_WEEK) - 2; // Monday=0, Sunday=6
        if (currentCol < MONDAYS_COL) {
            // adjust to mondays as first column
            currentCol = SUNDAYS_COL;
        }

        return currentCol;
    }

    protected static void applyCustomStyleToHeaderCell(final Workbook newExcel, final Cell headerCell) {
        final Font headerFont = newExcel.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        final CellStyle headerStyle = newExcel.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCell.setCellStyle(headerStyle);
    }

    protected static void createHeaderCell(final String involved, final String monthName, final int templateYear, final Sheet sheet) {
        final String headerText = involved + " - " + monthName + " " + templateYear;
        Row headerRow = createNewExcelRow(sheet, 0);

        final Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue(headerText);

        applyCustomStyleToHeaderCell(sheet.getWorkbook(), headerCell);
    }

}