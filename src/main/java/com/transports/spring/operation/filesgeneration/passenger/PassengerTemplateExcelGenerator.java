package com.transports.spring.operation.filesgeneration.passenger;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.operation.filesgeneration.common.AbstractExcelTemplateFileGenerator;
import com.transports.spring.operation.filesgeneration.common.AbstractTemplateFileGenerator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public final class PassengerTemplateExcelGenerator extends AbstractExcelTemplateFileGenerator {

    public Path generateExcel(final String passengerFullName, final String monthName, final int templateYear, final int templateMonth, final List<DtoPassengerTransport> allPassengerTransportsFromTemplate) throws IOException {
        /*super.generateSheet();
        //TODO think about maybe creating a PassengerTemplateExcelHeader for the line below
        createHeaderCell(passengerFullName, monthName, templateYear, sheet);
        //TODO think about maybe creating a PassengerTemplateExcelCalendar for the two lines below
        super.getCalendarOfTheMonth(templateYear, templateMonth);
        final int lastMonthDay = this.templateMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //TODO think about maybe creating a PassengerTemplateExcelCalendarIterator for the lines below
        // write templateMonth days
        int currentRow = START_ROW_DAYS;
        int currentCol = getInitialDayOfTheMonthColumn(this.templateMonthCalendar, MONDAYS_COL);

        for (int currentDayOfMonth = 1; currentDayOfMonth <= lastMonthDay; currentDayOfMonth++) {
            if (currentCol == OUT_OF_DAYS_COLUM_SCOPE) {
                currentCol = MONDAYS_COL;
                currentRow++;
            }

            final Row row = createNewExcelRow(sheet, currentRow);
            final Cell dayCell = row.createCell(currentCol);
            dayCell.setCellValue(currentDayOfMonth);

            // verify if it has an extra day
            for (final DtoPassengerTransport dtoPassengerTransport : allPassengerTransportsFromTemplate) {
                final String transportDateString = dtoPassengerTransport.getTransportDate();
                final LocalDate transportDate = LocalDate.parse(transportDateString);

                if (transportDate.getDayOfMonth() == currentDayOfMonth) {
                    final String eventName = dtoPassengerTransport.getEventName();
                    final String driverName = dtoPassengerTransport.getDriverFullName();

                    dayCell.setCellValue(currentDayOfMonth + " " + eventName);
                    final CellStyle cellStyle = getCellStyleForTransportCell(newExcel, dayCell);
                    final Row nextRow = createNewExcelRow(sheet, currentRow + 1);

                    final Cell nameCell = nextRow.createCell(currentCol);
                    nameCell.setCellValue(driverName);
                    nameCell.setCellStyle(cellStyle);
                }

            }
            currentCol++;
        }

        final Path tempPassengerCalendar = createTempInvolvedExcelFromExisting(originalCalendarPath, passengerFullName);
        super.writeInExcel(tempPassengerCalendar);

        return tempPassengerCalendar;*/
        return null;
    }

}