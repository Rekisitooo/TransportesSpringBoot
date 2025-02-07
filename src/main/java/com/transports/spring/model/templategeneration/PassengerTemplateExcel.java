package com.transports.spring.model.templategeneration;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PassengerTemplateExcel extends AbstractTemplateExcel {

    public PassengerTemplateExcel(PassengerTemplateExcelBody passengerTemplateExcelBody) throws IOException {
        super(passengerTemplateExcelBody, new PassengerTemplateExcelHeader());
    }
}
