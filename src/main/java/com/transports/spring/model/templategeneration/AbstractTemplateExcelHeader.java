package com.transports.spring.model.templategeneration;

import org.apache.poi.ss.usermodel.*;

public abstract class AbstractTemplateExcelHeader {

    protected static final int START_ROW_DAYS = 3;

    protected final Row row;
    protected final Cell cell;
    protected final Font headerFont;
    protected final CellStyle headerStyle;

    //START_ROW_DAYS
    protected AbstractTemplateExcelHeader(final Sheet excelSheet, final int currentRow) {
        this.row = excelSheet.createRow(currentRow);
        this.cell = this.row.createCell(0);

        final Workbook excel = excelSheet.getWorkbook();
        this.headerFont = excel.createFont();
        this.headerStyle = excel.createCellStyle();
    }

    protected void generate(final String involved, final String monthName, final int templateYear) {
        final String headerText = involved + " - " + monthName + " " + templateYear;
        this.cell.setCellValue(headerText);
        applyCustomStyleToHeaderCell();
    }

    protected void applyCustomStyleToHeaderCell() {
        createFont();
        addStylesToCell();
        this.cell.setCellStyle(headerStyle);
    }

    protected void createFont() {
        this.headerFont.setBold(true);
        this.headerFont.setFontHeightInPoints((short) 14);
    }

    protected void addStylesToCell() {
        this.headerStyle.setFont(this.headerFont);
        this.headerStyle.setAlignment(HorizontalAlignment.CENTER);
        this.headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }
}
