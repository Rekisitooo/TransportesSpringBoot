package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.model.templategeneration.common.AbstractTemplateExcel;

import java.io.IOException;
import java.nio.file.Path;

public class DriverTemplateExcel extends AbstractTemplateExcel {

    private final DriverTemplateExcelBody driverTemplateExcelBody;

    public DriverTemplateExcel(DriverTemplateExcelBody driverTemplateExcelBody) throws IOException {
        super(new DriverTemplateExcelHeader());
        this.driverTemplateExcelBody = driverTemplateExcelBody;
    }

    public void generate(final DtoTemplateExcelDriverBody templateExcelDriverBody, final DtoTemplateExcelHeader dtoTemplateExcelHeader, final Path excelPath) throws IOException {
        this.templateExcelHeader.generate(dtoTemplateExcelHeader);
        this.driverTemplateExcelBody.generate(this.sheet, templateExcelDriverBody);

        final String fullExcelPath = excelPath.toString();
        super.writeInExcel(fullExcelPath);
    }
}
