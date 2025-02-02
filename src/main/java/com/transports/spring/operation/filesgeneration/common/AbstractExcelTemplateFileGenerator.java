package com.transports.spring.operation.filesgeneration.common;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

public abstract class AbstractExcelTemplateFileGenerator {

    protected static final int SUNDAYS_COL = 6;
    protected static final String CALENDAR_NAME_FILE = "calendar";
    protected static final String XSL_EXTENSION = ".xlxs";
    protected static final int START_ROW_DAYS = 3;// Primera columna
    protected static final String SEPARATION_CHARACTER = "_";
    protected static final int OUT_OF_DAYS_COLUM_SCOPE = 7;
    protected static final int MONDAYS_COL = 0;
    protected static final String ORIGINAL_CALENDAR_FILE = CALENDAR_NAME_FILE + XSL_EXTENSION;

    protected Workbook newExcel;
    protected Sheet sheet;
    protected Path originalCalendarPath;
    protected FileInputStream fileInputStream;
    protected Calendar templateMonthCalendar;

    protected AbstractExcelTemplateFileGenerator(){}

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

    protected static int getInitialDayOfTheMonthColumn(final Calendar templateMonthCalendar, final int mondayCol) {
        int currentCol = templateMonthCalendar.get(Calendar.DAY_OF_WEEK) - 2; // Monday=0, Sunday=6
        if (currentCol < mondayCol) {
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

    /**
     * Creates a temporary file from an existing file.
     *
     * @param originalCalendar the file to copy from
     * @return the path to the newly created temporary file
     * @throws IOException if an I/O error occurs
     */
    protected static Path createTempInvolvedExcelFromExisting(final Path originalCalendar, final String involvedFullName) throws IOException {
        final Path tempFile = Files.createTempFile(involvedFullName, ".xlsx");
        Files.copy(originalCalendar, tempFile, StandardCopyOption.REPLACE_EXISTING);

        return tempFile;
    }

    protected void generateSheet() throws IOException {
        this.originalCalendarPath = Paths.get(ORIGINAL_CALENDAR_FILE);
        final File originalCalendarFile = originalCalendarPath.getFileName().toFile();
        this.fileInputStream = new FileInputStream(originalCalendarFile);
        this.newExcel = new HSSFWorkbook(fileInputStream);
        this.sheet = newExcel.getSheetAt(0);
    }

    protected void writeInExcel(final Path tempPassengerCalendar) throws IOException {
        final File passengerCalendar = tempPassengerCalendar.getFileName().toFile();
        final FileOutputStream fileOutputStream = new FileOutputStream(passengerCalendar);
        this.newExcel.write(fileOutputStream);
        fileOutputStream.close();
        this.newExcel.close();
        this.fileInputStream.close();
    }

    protected void getCalendarOfTheMonth(int templateYear, int templateMonth) {
        this.templateMonthCalendar = Calendar.getInstance();
        this.templateMonthCalendar.set(templateYear, templateMonth - 1, 1);
    }
}