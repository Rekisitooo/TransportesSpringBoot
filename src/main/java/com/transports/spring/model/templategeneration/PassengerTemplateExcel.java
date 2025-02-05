package com.transports.spring.model.templategeneration;

import java.io.IOException;

public class PassengerTemplateExcel extends AbstractTemplateExcel {

    public PassengerTemplateExcel(int templateYear, int templateMonth) throws IOException {
        super(
                new PassengerTemplateExcelBody(templateMonth, templateYear),
                new PassengerTemplateExcelHeader()
        );
    }
}
