package com.transports.spring.model.templategeneration;

import org.apache.poi.ss.usermodel.*;

public abstract class AbstractTemplateExcelTransportDayCell {

    protected static final int START_ROW_DAYS = 3;

    protected final Row row;
    protected final Cell cell;
    protected final Font font;
    protected final CellStyle cellStyle;


    protected AbstractTemplateExcelTransportDayCell(final Sheet excelSheet, final int currentRow) {
        this.row = excelSheet.createRow(currentRow);
        this.cell = this.row.createCell(0);

        final Workbook excel = excelSheet.getWorkbook();
        this.font = excel.createFont();
        this.cellStyle = excel.createCellStyle();
    }

    protected void generate(final String transportDayCellText) {
        this.cell.setCellValue(transportDayCellText);
        applyCustomStyleToTransportCell();
    }

    protected void createFont() {
        this.font.setColor(IndexedColors.DARK_BLUE.getIndex());
        this.font.setBold(true);
    }

    protected void addStylesToCell() {
        this.cellStyle.setFont(font);
        this.cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        this.cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    protected void applyCustomStyleToTransportCell() {
        createFont();
        addStylesToCell();
        this.cell.setCellStyle(this.cellStyle);
    }
}
