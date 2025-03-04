package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.model.templategeneration.common.cell.styler.AbstractDateCellGroupStyler;
import org.apache.poi.xssf.usermodel.*;

public class DayCellGroupNumberCell extends AbstractDayCellGroupCell {

    public DayCellGroupNumberCell(final XSSFSheet excelSheet, final int currentRow, final int currentCol) {
        super(excelSheet, currentRow, currentCol);
    }

    public void generate(final String currentDayOfMonth, final AbstractDateCellGroupStyler cellStyler) {
        final XSSFCellStyle cellStyle = cellStyler.getNumberHeaderCellStyle(this.excelSheet.getWorkbook());
        super.generateCellGroupBodyCell(this.excelSheet, this.currentRow, this.currentCol, currentDayOfMonth, cellStyle);
    }
}
