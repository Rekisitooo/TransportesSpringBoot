package com.transports.spring.model.templategeneration.common;

import lombok.Setter;
import org.apache.poi.xssf.usermodel.*;

import java.time.LocalDate;
import java.util.Calendar;
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

    protected LocalDate templateDate;
    protected Calendar templateMonthCalendar;
    protected Integer currentCol;
    protected int currentRow;
    protected final Integer lastMonthDay;

    protected AbstractTemplateExcelBodyGenerator(final Calendar templateMonthCalendar, final LocalDate templateDate) {
        this.templateMonthCalendar = templateMonthCalendar;
        this.templateDate = templateDate;
        this.currentCol = COLUMN_INDEX_FOR_DAY_OF_WEEK.get(templateMonthCalendar.get(Calendar.DAY_OF_WEEK));
        if (this.currentCol < MONDAY_COL_INDEX) {
            this.currentCol = SUNDAY_COL_INDEX;
        }

        this.currentRow = START_ROW_DAYS;
        this.lastMonthDay = templateMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    protected XSSFCell getDayNumberCell(final XSSFSheet excelSheet, final int currentDayOfMonth) {
        final XSSFRow row = excelSheet.getRow(this.currentRow);
        final XSSFCell dayCell = row.getCell(this.currentCol);
        dayCell.setCellValue(currentDayOfMonth);

        final XSSFCellStyle cellStyle = dayCell.getCellStyle();
        cellStyle.setIndention((short) 1);

        return dayCell;
    }

    protected void jumpToNextRowIfOutOfScope() {
        if (this.currentCol > SUNDAY_COL_INDEX) {
            this.currentCol = MONDAY_COL_INDEX;
            this.currentRow += 3;
        }
    }
}
