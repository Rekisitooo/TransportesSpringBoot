package com.transports.spring.operation;

import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.exception.CreatingTemplateFileException;
import com.transports.spring.model.*;
import com.transports.spring.service.InvolvedByTemplateService;
import com.transports.spring.service.MonthService;
import com.transports.spring.service.TemplateService;
import com.transports.spring.service.TransportService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;

import java.io.*;
import java.util.Calendar;
import java.util.List;

public final class GenerateTemplateFilesService implements IGenerateTemplateFilesService {

    public static final int START_ROW_DAYS = 3;// Primera columna
    public static final int START_COL_DAYS = 0;
    private static final String CALENDAR_NAME_FILE = "calendar";
    private static final String XSL_EXTENSION = ".xlxs";
    private static final String SEPARATION_CHARACTER = "_";
    public static final int MONDAYS_COL = 0;
    public static final int SUNDAYS_COL = 6;
    public static final int OUT_OF_DAYS_COLUM_SCOPE = 7;

    private final MonthService monthService;
    private final TemplateService templateService;
    private final TransportService transportService;
    private final InvolvedByTemplateService involvedByTemplateService;

    public GenerateTemplateFilesService(MonthService monthService, TemplateService templateService, TransportService transportService, InvolvedByTemplateService involvedByTemplateService) {
        this.monthService = monthService;
        this.templateService = templateService;
        this.transportService = transportService;
        this.involvedByTemplateService = involvedByTemplateService;
    }

    public GenerateTemplateFilesService() {}

    public void generateFiles(final int templateId) throws IOException, CreatingTemplateFileException {
        final Template template = this.templateService.findById(templateId);

        final String year = template.getYear();
        final int templateYear = Integer.parseInt(year);
        final String month = template.getMonth();
        final int templateMonth = Integer.parseInt(month);
        final Month templateMonthObj = this.monthService.findById(templateMonth);

        //first, passengers
        String outputFilePath;

        final List<Passenger> passengerList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : passengerList) {
            final List<Transport> allPassengerTransportsFromTemplate = this.transportService.findAllPassengerTransportsFromTemplate(passenger.getId(), templateId);
            generateExcel(templateMonthObj.getName(), templateYear, templateMonth, passenger);
        }

        //then, drivers

    }

    private static void generateExcel(String monthName, final int templateYear, final int templateMonth, final AbstractInvolved involved) throws CreatingTemplateFileException, IOException {
        final File calendarFile = new File(CALENDAR_NAME_FILE + XSL_EXTENSION);
        final FileInputStream fileInputStream = new FileInputStream(calendarFile);

        final String involvedFullName = involved.getFullName();

        try (Workbook newExcel = new HSSFWorkbook(fileInputStream)) {
            final Sheet sheet = newExcel.getSheetAt(0);
            createHeaderCell(involvedFullName, monthName, templateYear, sheet);

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
                final Cell cell = row.createCell(currentCol);
                cell.setCellValue(currentDayOfMonth);

                // verify if it has an extra day
                List<DtoTemplateDay> extraDays = null; //TODO retrieve
                for (final DtoTemplateDay extraDay : extraDays) {
                    if (extraDay.getDate().getDayOfMonth() == currentDayOfMonth) {
                        final String eventName = extraDay.getEventName();
                        final List<AbstractInvolved> involvedList = extraDay.getInvolvedList();

                        cell.setCellValue(currentDayOfMonth + " " + eventName);
                        final CellStyle cellStyle = getCellStyleForTransportCell(newExcel, cell);
                        final Row nextRow = createNewExcelRow(sheet, currentRow + 1);

                        final Cell nameCell = nextRow.createCell(currentCol);
                        String involvedsString = getInvolvedsString(involvedList);
                        nameCell.setCellValue(involvedsString);
                        nameCell.setCellStyle(cellStyle);
                    }
                }

                currentCol++;
            }

            String outputFilePath = templateYear + SEPARATION_CHARACTER + templateMonth + SEPARATION_CHARACTER + involvedFullName + XSL_EXTENSION;
            final File excelFile = new File(outputFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
            newExcel.write(fileOutputStream);
            fileOutputStream.close();

            newExcel.close();
        } catch (final IOException e) {
            throw new CreatingTemplateFileException(e);
        }
        fileInputStream.close();

        System.out.println("Archivo de " + involvedFullName + " guardado.");

    }

    private static CellStyle getCellStyleForTransportCell(final Workbook newExcel, final Cell cell) {
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

    private static Row createNewExcelRow(final Sheet sheet, final int currentRow) {
        Row row = sheet.getRow(currentRow);
        if (row == null) {
            row = sheet.createRow(currentRow);
        }
        return row;
    }

    private static int getInitialDayOfTheMonthColumn(final Calendar templateMonthCalendar) {
        int currentCol = templateMonthCalendar.get(Calendar.DAY_OF_WEEK) - 2; // Monday=0, Sunday=6
        if (currentCol < MONDAYS_COL) {
            // adjust to mondays as first column
            currentCol = SUNDAYS_COL;
        }

        return currentCol;
    }

    private static String getInvolvedsString(final List<AbstractInvolved> involvedList) {
        final StringBuilder sbInvolved = new StringBuilder();
        for (final AbstractInvolved involved1 : involvedList) {
            sbInvolved.append(involved1);
            sbInvolved.append(",");
        }
        String involvedsString = sbInvolved.toString();
        return involvedsString.substring(0, involvedsString.length() - 1);
    }

    private static void applyCustomStyleToHeaderCell(final Workbook newExcel, final Cell headerCell) {
        final Font headerFont = newExcel.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        final CellStyle headerStyle = newExcel.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCell.setCellStyle(headerStyle);
    }

    private static void createHeaderCell(final String involved, final String monthName, final int templateYear, final Sheet sheet) {
        final String headerText = involved + " - " + monthName + " " + templateYear;
        Row headerRow = createNewExcelRow(sheet, 0);

        final Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue(headerText);

        applyCustomStyleToHeaderCell(sheet.getWorkbook(), headerCell);
    }

}