package com.transports.spring.model.templategeneration;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public class PassengerTemplateExcelTransportDayCell extends AbstractTemplateExcelTransportDayCell {

    protected PassengerTemplateExcelTransportDayCell(final XSSFSheet excelSheet, final int currentRow, final int currentCol) {
        super(excelSheet, currentRow, currentCol);
    }
}
