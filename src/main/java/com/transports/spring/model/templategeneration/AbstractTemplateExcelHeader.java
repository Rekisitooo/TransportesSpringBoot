package com.transports.spring.model.templategeneration;

import org.apache.poi.xssf.usermodel.*;

public abstract class AbstractTemplateExcelHeader {

    protected XSSFRow row;
    protected XSSFCell cell;

    protected AbstractTemplateExcelHeader() {}

    protected void generate(final String involved, final String monthName, final int templateYear) {
        final String headerText = involved + " - " + monthName + " " + templateYear;
        this.cell.setCellValue(headerText);
    }

    protected AbstractTemplateExcelHeader init(final XSSFSheet excelSheet, final int currentRow) {
        this.row = excelSheet.getRow(currentRow);
        this.cell = this.row.getCell(2);
        return this;
    }
}
