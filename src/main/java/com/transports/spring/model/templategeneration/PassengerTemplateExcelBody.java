package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.IDtoInvolvedTransport;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class PassengerTemplateExcelBody extends AbstractTemplateExcelBody {

    protected PassengerTemplateExcelBody(int templateYear, int templateMonth) {
        super(templateYear, templateMonth, new PassengerTemplateExcelBodyGenerator());
    }

    @Override
    public void generate(final Sheet excelSheet, final List<IDtoInvolvedTransport> allInvolvedTransportsFromTemplate) {
        super.generate(excelSheet, allInvolvedTransportsFromTemplate);
    }
}
