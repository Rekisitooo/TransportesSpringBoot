package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.List;

public class PassengerTemplateFile extends AbstractTemplateFile {
    private final PassengerTemplateExcel passengerTemplateExcel;
    private final PassengerTemplateJpg passengerTemplateJpg;

    public PassengerTemplateFile() throws IOException {
        super();
        this.passengerTemplateExcel = new PassengerTemplateExcel();
        this.passengerTemplateJpg = new PassengerTemplateJpg();
    }

    public void generate(final List<DtoPassengerTransport> allInvolvedTransportsFromTemplate, final String passengerFullName, final String monthName, final int templateYear){
        this.passengerTemplateExcel.generate(super.sheet, allInvolvedTransportsFromTemplate, passengerFullName, monthName, templateYear);
        this.passengerTemplateJpg.generate();
    }
}
