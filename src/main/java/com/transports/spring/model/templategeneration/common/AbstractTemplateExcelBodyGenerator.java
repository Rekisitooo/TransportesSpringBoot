package com.transports.spring.model.templategeneration.common;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupBodyCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupHeaderCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupNumberCell;
import com.transports.spring.model.templategeneration.driver.TemplateExcelTransportDayCellGroup;
import lombok.Setter;
import org.apache.poi.xssf.usermodel.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
public abstract class AbstractTemplateExcelBodyGenerator {

    protected static final int DAY_CELL_GROUP_LENGTH_COLS = 2;
    protected static final int START_ROW_DAYS = 3;
    protected static final int MONDAY_COL_INDEX = 0;
    protected static final int SUNDAY_COL_INDEX = MONDAY_COL_INDEX + (DAY_CELL_GROUP_LENGTH_COLS * 6);
    protected static final Map<Integer, Integer> COLUMN_INDEX_FOR_DAY_OF_WEEK = Stream.of(new Integer[][] {
        { Calendar.MONDAY, MONDAY_COL_INDEX },
        { Calendar.TUESDAY, (MONDAY_COL_INDEX + (DAY_CELL_GROUP_LENGTH_COLS * 1)) },
        { Calendar.WEDNESDAY, (MONDAY_COL_INDEX + (DAY_CELL_GROUP_LENGTH_COLS * 2)) },
        { Calendar.THURSDAY, (MONDAY_COL_INDEX + (DAY_CELL_GROUP_LENGTH_COLS * 3)) },
        { Calendar.FRIDAY, (MONDAY_COL_INDEX + (DAY_CELL_GROUP_LENGTH_COLS * 4)) },
        { Calendar.SATURDAY, (MONDAY_COL_INDEX + (DAY_CELL_GROUP_LENGTH_COLS * 5)) },
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

    protected void generateCustomTemplateExcelTransportDayCellGroup(final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup, final XSSFSheet excelSheet) {
        final DayCellGroupNumberCell dayCellGroupNumberCell = new DayCellGroupNumberCell(excelSheet, this.currentRow, this.currentCol);
        final DayCellGroupHeaderCell dayCellGroupHeaderCell = new DayCellGroupHeaderCell(excelSheet, this.currentRow + 1, this.currentCol);
        final DayCellGroupBodyCell dayCellGroupBodyCell = new DayCellGroupBodyCell(excelSheet, this.currentRow + 2, this.currentCol);
        final TemplateExcelTransportDayCellGroup transportDayCell = new TemplateExcelTransportDayCellGroup(dayCellGroupNumberCell, dayCellGroupHeaderCell, dayCellGroupBodyCell);

        transportDayCell.generate(dtoTemplateExcelTransportCellGroup);
    }
}
