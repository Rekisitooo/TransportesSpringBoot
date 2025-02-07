package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Calendar;
import java.util.List;

public abstract class AbstractTemplateExcelBody {
    protected final AbstractTemplateExcelBodyGenerator templateExcelBodyGenerator;

    protected AbstractTemplateExcelBody(final AbstractTemplateExcelBodyGenerator bodyGenerator) {
        this.templateExcelBodyGenerator = bodyGenerator;
    }

    protected void generate(final XSSFSheet excelSheet, final List<DtoInvolvedTransport> allInvolvedTransportsFromTemplate) {
        this.templateExcelBodyGenerator.generate(excelSheet, allInvolvedTransportsFromTemplate);
    }

}
