package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Calendar;
import java.util.List;

public abstract class AbstractTemplateExcelBody {
    protected final Calendar templateMonthCalendar;
    protected final AbstractTemplateExcelBodyGenerator templateExcelBodyIterator;

    protected AbstractTemplateExcelBody(int templateYear, int templateMonth, final AbstractTemplateExcelBodyGenerator bodyGenerator) {
        this.templateMonthCalendar = Calendar.getInstance();
        this.templateMonthCalendar.set(templateYear, templateMonth - 1, 1);
        this.templateExcelBodyIterator = bodyGenerator;
    }

    protected void generate(final Sheet excelSheet, final List<DtoInvolvedTransport> allInvolvedTransportsFromTemplate) {
        this.templateExcelBodyIterator.generate(excelSheet, allInvolvedTransportsFromTemplate);
    }
}
