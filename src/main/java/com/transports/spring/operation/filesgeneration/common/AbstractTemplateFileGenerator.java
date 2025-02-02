package com.transports.spring.operation.filesgeneration.common;

import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;

public abstract class AbstractTemplateFileGenerator {

    protected AbstractTemplateFileGenerator(){}

    protected static CellStyle getCellStyleForTransportCell(final Workbook newExcel, final Cell cell) {
        return AbstractExcelTemplateFileGenerator.getCellStyleForTransportCell(newExcel, cell);
    }

    protected static Row createNewExcelRow(final Sheet sheet, final int currentRow) {
        return AbstractExcelTemplateFileGenerator.createNewExcelRow(sheet, currentRow);
    }

    protected static int getInitialDayOfTheMonthColumn(final Calendar templateMonthCalendar) {
        return AbstractExcelTemplateFileGenerator.getInitialDayOfTheMonthColumn(templateMonthCalendar, MONDAYS_COL);
    }

    protected static void applyCustomStyleToHeaderCell(final Workbook newExcel, final Cell headerCell) {
        AbstractExcelTemplateFileGenerator.applyCustomStyleToHeaderCell(newExcel, headerCell);
    }

    protected static void createHeaderCell(final String involved, final String monthName, final int templateYear, final Sheet sheet) {
        AbstractExcelTemplateFileGenerator.createHeaderCell(involved, monthName, templateYear, sheet);
    }

    protected void generateJPG(final Path involvedCalendarPath) {
        AbstractJpgTemplateFileGenerator.generateJPG(involvedCalendarPath);
    }


    protected Path createTempInvolvedExcelFromExisting(final Path originalCalendar, final String involvedFullName) throws IOException {
        return AbstractExcelTemplateFileGenerator.createTempInvolvedExcelFromExisting(originalCalendar, involvedFullName);
    }
}