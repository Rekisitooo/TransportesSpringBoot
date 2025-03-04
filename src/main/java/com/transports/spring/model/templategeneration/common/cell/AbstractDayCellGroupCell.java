package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.model.templategeneration.common.cell.styler.AbstractDateCellGroupStyler;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class AbstractDayCellGroupCell {

    protected XSSFCell cell;
    protected XSSFSheet excelSheet;
    protected int currentRow;
    protected int currentCol;

    protected AbstractDayCellGroupCell() {
        this.cell = null;
    }

    protected AbstractDayCellGroupCell(final XSSFSheet excelSheet, final int currentRow, final int currentCol) {
        this.excelSheet = excelSheet;
        this.currentCol = currentCol;
        this.currentRow = currentRow;
        this.cell = null;
    }

    protected void generateCellGroupBodyCell(final XSSFSheet excelSheet, final int currentRow, final int currentCol, final String cellText, final XSSFCellStyle cellStyle) {
        final XSSFRow row = excelSheet.getRow(currentRow);
        this.cell = row.getCell(currentCol);
        this.cell.setCellStyle(cellStyle);
        this.cell.setCellValue(cellText);
    }

}
