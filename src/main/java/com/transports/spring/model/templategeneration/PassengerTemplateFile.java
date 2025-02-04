package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.IDtoInvolvedTransport;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.List;

public class PassengerTemplateFile extends AbstractTemplateFile {
    private final PassengerTemplateExcel passengerTemplateExcel;
    private final PassengerTemplateJpg passengerTemplateJpg;

    public PassengerTemplateFile(final int teamplateYear, final int templateMonth) throws IOException {
        super();
        this.passengerTemplateExcel = new PassengerTemplateExcel(teamplateYear, templateMonth);
        this.passengerTemplateJpg = new PassengerTemplateJpg();
    }

    public void generate(final List<IDtoInvolvedTransport> allInvolvedTransportsFromTemplate, final String passengerFullName, final String monthName, final int templateYear){
        this.passengerTemplateExcel.generate(super.sheet, allInvolvedTransportsFromTemplate, passengerFullName, monthName, templateYear);
        this.passengerTemplateJpg.generate();
    }
}
