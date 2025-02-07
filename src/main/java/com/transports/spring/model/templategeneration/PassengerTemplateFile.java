package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class PassengerTemplateFile extends AbstractTemplateFile {

    public PassengerTemplateFile(PassengerTemplateExcel passengerTemplateExcel, PassengerTemplateJpg passengerTemplateJpg) {
        super(passengerTemplateExcel, passengerTemplateJpg);
    }

    public void generate(final List<DtoInvolvedTransport> allInvolvedTransportsFromTemplate, final String passengerFullName, final String monthName, final int templateYear) throws IOException {
        final Path involvedExcelCalendar = super.templateExcel.generate(allInvolvedTransportsFromTemplate, passengerFullName, monthName, templateYear);
        super.templateJpg.generate(involvedExcelCalendar);
    }
}
