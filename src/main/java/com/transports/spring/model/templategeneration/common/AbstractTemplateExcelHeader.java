package com.transports.spring.model.templategeneration.common;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import org.apache.poi.xssf.usermodel.*;

public abstract class AbstractTemplateExcelHeader {

    public static final int HEADER_COL = 0;
    protected XSSFRow row;
    protected XSSFCell cell;

    protected AbstractTemplateExcelHeader() {}

    public void generate(final DtoTemplateExcelHeader dtoTemplateExcelHeader) {
        final String headerText =
                dtoTemplateExcelHeader.getInvolvedFullName() +
                        " - " +
                        dtoTemplateExcelHeader.getMonthName() +
                        " " +
                        dtoTemplateExcelHeader.getTemplateYear();

        this.cell.setCellValue(headerText);
    }

    protected AbstractTemplateExcelHeader init(final XSSFSheet excelSheet, final int currentRow) {
        this.row = excelSheet.getRow(currentRow);
        this.cell = this.row.getCell(HEADER_COL);
        return this;
    }
}
