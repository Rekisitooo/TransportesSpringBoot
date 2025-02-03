package com.transports.spring.model.templategeneration;

import org.apache.poi.ss.usermodel.*;

public class PassengerTemplateExcelHeader extends AbstractTemplateExcelHeader {

    //START_ROW_DAYS
    public PassengerTemplateExcelHeader(final Sheet excelSheet, final int currentRow) {
        super(excelSheet, currentRow);
    }

    protected void generate(final String involvedFullName, final String monthName, final int templateYear) {
        super.generate(involvedFullName, monthName, templateYear);
    }
}
