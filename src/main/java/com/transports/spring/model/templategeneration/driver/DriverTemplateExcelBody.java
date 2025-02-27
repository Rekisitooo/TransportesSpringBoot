package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DriverTemplateExcelBody {

    public final DriverTemplateExcelBodyGenerator driverExcelBodyGenerator;

    public DriverTemplateExcelBody(final DriverTemplateExcelBodyGenerator bodyGenerator) {
        this.driverExcelBodyGenerator = bodyGenerator;
    }

    protected void generate(final XSSFSheet excelSheet, final DtoTemplateExcelDriverBody templateExcelDriverBody) {
        this.driverExcelBodyGenerator.generate(excelSheet, templateExcelDriverBody);
    }
}
