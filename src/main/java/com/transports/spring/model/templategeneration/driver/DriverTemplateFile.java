package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.model.templategeneration.common.AbstractTemplateFile;

import java.io.IOException;
import java.nio.file.Path;

public class DriverTemplateFile extends AbstractTemplateFile {

    private final DriverTemplateExcel driverTemplateExcel;

    public DriverTemplateFile(DriverTemplateExcel driverTemplateExcel, DriverTemplateJpg driverTemplateJpg) {
        super(driverTemplateJpg);
        this.driverTemplateExcel = driverTemplateExcel;
    }

    public void generate(final DtoTemplateExcelDriverBody templateExcelDriverBody, final DtoTemplateExcelHeader dtoTemplateExcelHeader) throws IOException {
        final Path involvedExcelCalendar = this.driverTemplateExcel.generate(templateExcelDriverBody, dtoTemplateExcelHeader);
        super.templateJpg.generate(involvedExcelCalendar);
    }
}
