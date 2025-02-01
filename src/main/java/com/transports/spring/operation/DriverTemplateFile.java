package com.transports.spring.operation;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.exception.CreatingTemplateFileException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public final class DriverTemplateFile extends AbstractTemplateFile {

    public static void generate(final List<DtoDriverTransport> driverTransportsFromTemplate, final String monthName, final int templateYear, final int templateMonth, final String driverFullName) throws CreatingTemplateFileException, IOException {
        final File calendarFile = new File(CALENDAR_NAME_FILE + XSL_EXTENSION);
        final FileInputStream fileInputStream = new FileInputStream(calendarFile);

        try {
            Workbook newExcel = new HSSFWorkbook(fileInputStream);

            final Sheet sheet = newExcel.getSheetAt(0);
            createHeaderCell(driverFullName, monthName, templateYear, sheet);

            // Generate templateMonth Calendar obj
            final Calendar templateMonthCalendar = Calendar.getInstance();
            templateMonthCalendar.set(templateYear, templateMonth - 1, 1);

            final int lastMonthDay = templateMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            // write templateMonth days
            int currentRow = START_ROW_DAYS;
            int currentCol = getInitialDayOfTheMonthColumn(templateMonthCalendar);

            for (int currentDayOfMonth = 1; currentDayOfMonth <= lastMonthDay; currentDayOfMonth++) {
                if (currentCol == OUT_OF_DAYS_COLUM_SCOPE) {
                    currentCol = MONDAYS_COL;
                    currentRow++;
                }

                final Row row = createNewExcelRow(sheet, currentRow);
                final Cell dayCell = row.createCell(currentCol);
                dayCell.setCellValue(currentDayOfMonth);

                // verify if it has an extra day
                for (final DtoDriverTransport dtoDriverTransport : driverTransportsFromTemplate) {
                    final String transportDateString = dtoDriverTransport.getTransportDate();
                    final LocalDate transportDate = LocalDate.parse(transportDateString);

                    if (transportDate.getDayOfMonth() == currentDayOfMonth) {
                        final String eventName = dtoDriverTransport.getEventName();
                        final List<String> passengers = dtoDriverTransport.getPassengers();
                        final String passengerNames = getPassengersString(passengers);

                        dayCell.setCellValue(currentDayOfMonth + " " + eventName);
                        final CellStyle cellStyle = getCellStyleForTransportCell(newExcel, dayCell);
                        final Row nextRow = createNewExcelRow(sheet, currentRow + 1);

                        final Cell nameCell = nextRow.createCell(currentCol);
                        nameCell.setCellValue(passengerNames);
                        nameCell.setCellStyle(cellStyle);
                    }

                }
                currentCol++;
            }

            final String outputFilePath = templateYear + SEPARATION_CHARACTER + templateMonth + SEPARATION_CHARACTER + driverFullName + XSL_EXTENSION;
            final File excelFile = new File(outputFilePath);
            final FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
            newExcel.write(fileOutputStream);
            fileOutputStream.close();

            newExcel.close();
        } catch (final IOException e) {
            throw new CreatingTemplateFileException(e);
        }
        fileInputStream.close();

        System.out.println("Archivo de " + driverFullName + " guardado.");

    }

    private static String getPassengersString(final List<String> passengerNamesList) {
        final StringBuilder sbPassengers = new StringBuilder();
        for (final String passengerName : passengerNamesList) {
            sbPassengers.append(passengerName);
            sbPassengers.append(",");
        }

        final String passengersString = sbPassengers.toString();
        final String passengersStringWithoutLastComma = passengersString.substring(0, passengersString.length() - 1);
        return passengersStringWithoutLastComma;
    }

}