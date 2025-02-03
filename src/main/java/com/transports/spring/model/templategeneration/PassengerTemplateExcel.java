package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoPassengerTransport;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.List;

public class PassengerTemplateExcel extends AbstractTemplateFile {
    private final PassengerTemplateExcelHeader passengerTemplateExcelHeader;
    private final PassengerTemplateExcelBody passengerTemplateExcelBody;

    public PassengerTemplateExcel(int templateYear, int templateMonth) throws IOException {
        super();
        this.passengerTemplateExcelBody = new PassengerTemplateExcelBody(templateMonth, templateYear);
        this.passengerTemplateExcelHeader = new PassengerTemplateExcelHeader(super.sheet, START_ROW_DAYS);
    }

    public void generate(final Sheet excelSheet, final List<DtoPassengerTransport> allInvolvedTransportsFromTemplate, final String passengerFullName, final String monthName, final int templateYear) {
        this.passengerTemplateExcelHeader.generate(passengerFullName, monthName, templateYear);
        this.passengerTemplateExcelBody.generate(excelSheet, allInvolvedTransportsFromTemplate);
    }
}
