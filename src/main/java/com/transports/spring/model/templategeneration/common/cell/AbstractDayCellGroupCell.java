package com.transports.spring.model.templategeneration.common.cell;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class AbstractDayCellGroupCell {

    protected XSSFCell cell;

    protected AbstractDayCellGroupCell() {
        this.cell = null;
    }

    protected void generate(final XSSFSheet excelSheet, final int currentRow, final int currentCol, final String cellText, final XSSFCellStyle cellStyle) {
        final XSSFRow row = excelSheet.getRow(currentRow);
        this.cell = row.getCell(currentCol);
        this.cell.setCellStyle(cellStyle);
        this.cell.setCellValue(cellText);
    }

}
