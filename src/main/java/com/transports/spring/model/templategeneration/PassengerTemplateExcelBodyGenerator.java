package com.transports.spring.model.templategeneration;

import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class PassengerTemplateExcelBodyGenerator extends AbstractTemplateExcelBodyGenerator {

    public PassengerTemplateExcelBodyGenerator(final Calendar templateMonthCalendar) {
        super(templateMonthCalendar);
    }
}
