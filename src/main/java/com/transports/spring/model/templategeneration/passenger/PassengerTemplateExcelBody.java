package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class PassengerTemplateExcelBody {

    public final PassengerTemplateExcelBodyGenerator passengerExcelBodyGenerator;

    public PassengerTemplateExcelBody(final PassengerTemplateExcelBodyGenerator bodyGenerator) {
        this.passengerExcelBodyGenerator = bodyGenerator;
    }

    protected void generate(final XSSFSheet excelSheet, final DtoTemplateExcelPassengerBody templateExcelPassengerBody) {
        this.passengerExcelBodyGenerator.generate(excelSheet, templateExcelPassengerBody);
    }
}
