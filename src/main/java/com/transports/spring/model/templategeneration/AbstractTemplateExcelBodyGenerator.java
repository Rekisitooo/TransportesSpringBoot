package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
public abstract class AbstractTemplateExcelBodyGenerator {

    protected static final int SUNDAY_COL_INDEX = 14;
    protected static final int MONDAY_COL_INDEX = 2;
    protected static final int START_ROW_DAYS = 4;

    protected static final Map<Integer, Integer> COLUMN_INDEX_FOR_DAY_OF_WEEK = Stream.of(new Integer[][] {
        { Calendar.MONDAY, MONDAY_COL_INDEX },
        { Calendar.TUESDAY, 4 },
        { Calendar.WEDNESDAY, 6 },
        { Calendar.THURSDAY, 8 },
        { Calendar.FRIDAY, 10 },
        { Calendar.SATURDAY, 12 },
        { Calendar.SUNDAY, SUNDAY_COL_INDEX }
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    protected Calendar templateMonthCalendar;
    protected Integer currentCol;
    protected int currentRow;
    protected final Integer lastMonthDay;
    private BeanFactory beanFactory;

    protected AbstractTemplateExcelBodyGenerator(final Calendar templateMonthCalendar) {
        this.templateMonthCalendar = templateMonthCalendar;
        this.currentCol = COLUMN_INDEX_FOR_DAY_OF_WEEK.get(templateMonthCalendar.get(Calendar.DAY_OF_WEEK));
        if (this.currentCol < MONDAY_COL_INDEX) {
            this.currentCol = SUNDAY_COL_INDEX;
        }

        this.currentRow = START_ROW_DAYS;
        this.lastMonthDay = templateMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public void generate(final XSSFSheet excelSheet, final List<DtoInvolvedTransport> allInvolvedTransportsFromTemplate) {
        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final XSSFCell dayCell = this.getDayCell(excelSheet, currentDayOfMonth);

            for (final DtoInvolvedTransport dtoInvolvedTransport : allInvolvedTransportsFromTemplate) {
                final String transportDateString = dtoInvolvedTransport.getTransportDate();
                final LocalDate transportDate = LocalDate.parse(transportDateString);

                if (transportDate.getDayOfMonth() == currentDayOfMonth) {
                    final String eventName = dtoInvolvedTransport.getEventName();
                    dayCell.setCellValue(currentDayOfMonth + " " + eventName);
                    final XSSFWorkbook workbook = excelSheet.getWorkbook();
                    final XSSFCellStyle cellStyle = workbook.createCellStyle();
                    final XSSFFont cellFont = workbook.createFont();
                    cellFont.setColor(TemplateExcelTransportDayCellStyler.BLUE_FONT_COLOR);
                    cellFont.setBold(true);
                    cellFont.setFontName("Aptos Narrow");
                    cellStyle.setFont(cellFont);
                    cellStyle.setFillForegroundColor(TemplateExcelTransportDayCellStyler.LIGHT_BLUE_BACKGROUND_COLOR);
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyle.setIndention((short) 1);
                    cellStyle.setBorderTop(BorderStyle.THIN);
                    dayCell.setCellStyle(cellStyle);

                    final XSSFRow row = excelSheet.getRow(this.currentRow + 1);
                    final XSSFCell dayCell2 = row.getCell(this.currentCol);
                    final XSSFCellStyle cellStyle2 = workbook.createCellStyle();
                    final XSSFFont cellFont2 = workbook.createFont();
                    cellFont2.setBold(true);
                    cellFont2.setFontName("Aptos Narrow");
                    cellStyle2.setFont(cellFont2);
                    cellStyle2.setBorderLeft(BorderStyle.THIN);
                    cellStyle2.setBorderRight(BorderStyle.THIN);
                    cellStyle2.setAlignment(HorizontalAlignment.LEFT);
                    cellStyle2.setIndention((short) 1);
                    cellStyle2.setFillForegroundColor(TemplateExcelTransportDayCellStyler.LIGHT_BLUE_BACKGROUND_COLOR);
                    cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    dayCell2.setCellStyle(cellStyle2);
                    dayCell2.setCellValue("Te lleva:");

                    final String transportDayCellText = dtoInvolvedTransport.getName();
                    PassengerTemplateExcelTransportDayCell transportDayCell = new PassengerTemplateExcelTransportDayCell(excelSheet, this.currentRow + 2, this.currentCol);
                    transportDayCell.generate(transportDayCellText);
                }

            }

            this.currentCol += 2;
        }
    }

    private XSSFCell getDayCell(final XSSFSheet excelSheet, final int currentDayOfMonth) {
        final XSSFRow row = excelSheet.getRow(this.currentRow);
        final XSSFCell dayCell = row.getCell(this.currentCol);
        dayCell.setCellValue(currentDayOfMonth);

        final XSSFCellStyle cellStyle = dayCell.getCellStyle();
        cellStyle.setIndention((short) 1);

        return dayCell;
    }

    private void jumpToNextRowIfOutOfScope() {
        if (this.currentCol > SUNDAY_COL_INDEX) {
            this.currentCol = MONDAY_COL_INDEX;
            this.currentRow += 3;
        }
    }

}
