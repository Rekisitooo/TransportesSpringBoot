package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.dto.IDtoInvolvedTransport;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Setter
public abstract class AbstractTemplateExcelBodyGenerator {

    protected static final int OUT_OF_DAYS_COLUM_SCOPE = 7;
    protected static final int SUNDAY_COL_INDEX = 6;
    protected static final int MONDAY_COL_INDEX = 0;
    protected static final int START_ROW_DAYS = 3;

    protected Integer currentCol;
    protected int currentRow;
    protected Integer lastMonthDay;

    protected AbstractTemplateExcelBodyGenerator() {
        this.lastMonthDay = null;
        this.currentRow = START_ROW_DAYS;
        this.currentCol = null;
    }

    private void buildInstance(final Calendar templateMonthCalendar) {
        setInitialCurrentCol(templateMonthCalendar);
        this.currentRow = START_ROW_DAYS;
        this.lastMonthDay = templateMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public void generate(final Calendar templateMonthCalendar, final Sheet excelSheet, final List<IDtoInvolvedTransport> allInvolvedTransportsFromTemplate) {
        buildInstance(templateMonthCalendar);
        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final Cell dayCell = createNewDayCell(excelSheet, currentDayOfMonth);

            // verify if it has an extra day
            for (final IDtoInvolvedTransport dtoInvolvedTransport : allInvolvedTransportsFromTemplate) {
                final String transportDateString = dtoInvolvedTransport.getTransportDate();
                final LocalDate transportDate = LocalDate.parse(transportDateString);

                if (transportDate.getDayOfMonth() == currentDayOfMonth) {
                    final String eventName = dtoInvolvedTransport.getEventName();
                    dayCell.setCellValue(currentDayOfMonth + " " + eventName);

                    final String transportDayCellText = dtoInvolvedTransport.getNamesToWriteInTransportDateCell();
                    PassengerTemplateExcelTransportDayCell transportDayCell = new PassengerTemplateExcelTransportDayCell(excelSheet, this.currentRow);
                    transportDayCell.generate(transportDayCellText);
                }

            }
            this.currentCol++;
        }
    }

    private Cell createNewDayCell(Sheet excelSheet, int currentDayOfMonth) {
        final Row row = excelSheet.createRow(this.currentRow);
        final Cell dayCell = row.createCell(this.currentRow);
        dayCell.setCellValue(currentDayOfMonth);
        return dayCell;
    }

    private void jumpToNextRowIfOutOfScope() {
        if (this.currentCol == OUT_OF_DAYS_COLUM_SCOPE) {
            this.currentCol = MONDAY_COL_INDEX;
            this.currentRow++;
        }
    }

    private void setInitialCurrentCol(final Calendar templateMonthCalendar) {
        this.currentCol = templateMonthCalendar.get(Calendar.DAY_OF_WEEK) - 2; // Monday=0, Sunday=6
        if (this.currentCol < MONDAY_COL_INDEX) {
            this.currentCol = SUNDAY_COL_INDEX;
        }
    }
}
