package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.model.templategeneration.common.cell.styler.AbstractDateCellGroupStyler;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DayCellGroupBodyCell extends AbstractDayCellGroupCell {

    public DayCellGroupBodyCell(final XSSFSheet excelSheet, final int currentRow, final int currentCol) {
        super(excelSheet, currentRow, currentCol);
    }

    public void generate(final String currentDayOfMonth, final AbstractDateCellGroupStyler cellStyler) {
        final XSSFCellStyle cellStyle = cellStyler.getBodyCellStyle(this.excelSheet.getWorkbook());
        super.generateCellGroupBodyCell(this.excelSheet, this.currentRow, this.currentCol, currentDayOfMonth, cellStyle);
    }
}
