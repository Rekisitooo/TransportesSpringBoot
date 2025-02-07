package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class PassengerTemplateExcelBody extends AbstractTemplateExcelBody {

    public PassengerTemplateExcelBody(final PassengerTemplateExcelBodyGenerator passengerTemplateExcelBodyGenerator) {
        super(passengerTemplateExcelBodyGenerator);
    }

    @Override
    public void generate(final XSSFSheet excelSheet, final List<DtoInvolvedTransport> allInvolvedTransportsFromTemplate) {
        super.generate(excelSheet, allInvolvedTransportsFromTemplate);
    }
}
