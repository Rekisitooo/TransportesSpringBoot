package com.transports.spring.model.templategeneration;

import java.util.Calendar;

public class PassengerTemplateExcelBodyGenerator extends AbstractTemplateExcelBodyGenerator {

    protected PassengerTemplateExcelBodyGenerator(Calendar templateMonthCalendar, int currentRow) {
        super(templateMonthCalendar, currentRow);
    }
}
