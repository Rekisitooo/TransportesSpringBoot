package com.transports.spring.model.templategeneration;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public abstract class AbstractTemplateExcelTransportDayCell {

    protected final XSSFRow row;
    protected final XSSFCell cell;
    protected final XSSFFont font;
    protected final XSSFCellStyle cellStyle;


    protected AbstractTemplateExcelTransportDayCell(final XSSFSheet excelSheet, final int currentRow, final int currentCol) {
        this.row = excelSheet.getRow(currentRow);
        this.cell = this.row.getCell(currentCol);
        this.cellStyle = excelSheet.getWorkbook().createCellStyle();
        this.font = this.cellStyle.getFont();
    }

    protected void generate(final String transportDayCellText) {
        this.cell.setCellValue(transportDayCellText);
        applyCustomStyleToTransportCell();
    }

    protected void createFont() {
        this.font.setColor(IndexedColors.DARK_BLUE.getIndex());
        this.font.setBold(false);
    }

    protected void addStylesToCell() {
        this.cellStyle.setFont(font);
        this.cellStyle.setFillForegroundColor(TemplateExcelTransportDayCellStyler.LIGHT_BLUE_BACKGROUND_COLOR);
        this.cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.cellStyle.setIndention((short) 1);
        this.cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        this.cellStyle.setBorderRight(BorderStyle.THIN);
        this.cellStyle.setBorderLeft(BorderStyle.THIN);
        this.cellStyle.setBorderBottom(BorderStyle.THIN);
    }

    protected void applyCustomStyleToTransportCell() {
        createFont();
        addStylesToCell();
        this.cell.setCellStyle(this.cellStyle);
    }

}
