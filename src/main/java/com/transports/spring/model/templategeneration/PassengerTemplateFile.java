package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PassengerTemplateFile extends AbstractTemplateFile {

    public PassengerTemplateFile(final int teamplateYear, final int templateMonth) throws IOException {
        super(
                new PassengerTemplateExcel(teamplateYear, templateMonth),
                new PassengerTemplateJpg()
        );
    }

    public void generate(final List<DtoInvolvedTransport> allInvolvedTransportsFromTemplate, final String passengerFullName, final String monthName, final int templateYear) throws IOException {
        final Path involvedExcelCalendar = super.templateExcel.generate(allInvolvedTransportsFromTemplate, passengerFullName, monthName, templateYear);
        super.templateJpg.generate(involvedExcelCalendar);
    }
}
