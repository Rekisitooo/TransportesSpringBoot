package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.model.templategeneration.common.AbstractTemplateExcel;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Getter
public class PassengerTemplateExcel extends AbstractTemplateExcel {

    private final PassengerTemplateExcelBody passengerTemplateExcelBody;

    public PassengerTemplateExcel(final PassengerTemplateExcelBody passengerTemplateExcelBody) throws IOException {
        super(new PassengerTemplateExcelHeader());
        this.passengerTemplateExcelBody = passengerTemplateExcelBody;
    }

    public Path generate(final DtoTemplateExcelPassengerBody templateExcelPassengerBody, final DtoTemplateExcelHeader dtoTemplateExcelHeader, final Path excelPath) throws IOException {
        this.templateExcelHeader.generate(dtoTemplateExcelHeader);
        this.passengerTemplateExcelBody.generate(this.sheet, templateExcelPassengerBody);

        final String fullExcelPath = excelPath.toString();
        final File passengerCalendar = new File(fullExcelPath);
        super.writeInExcel(fullExcelPath);

        return passengerCalendar.toPath();
    }
}
