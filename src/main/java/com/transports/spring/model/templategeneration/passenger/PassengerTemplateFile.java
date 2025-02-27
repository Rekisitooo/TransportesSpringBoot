package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.model.templategeneration.common.AbstractTemplateFile;

import java.io.IOException;
import java.nio.file.Path;

public class PassengerTemplateFile extends AbstractTemplateFile {

    private final PassengerTemplateExcel passengerTemplateExcel;

    public PassengerTemplateFile(PassengerTemplateExcel passengerTemplateExcel, PassengerTemplateJpg passengerTemplateJpg) {
        super(passengerTemplateJpg);
        this.passengerTemplateExcel = passengerTemplateExcel;
    }

    public void generate(final DtoTemplateExcelPassengerBody templateExcelPassengerBody, final DtoTemplateExcelHeader dtoHeader) throws IOException {
        final Path involvedExcelCalendar = this.passengerTemplateExcel.generate(templateExcelPassengerBody, dtoHeader);
        super.templateJpg.generate(involvedExcelCalendar);
    }
}
