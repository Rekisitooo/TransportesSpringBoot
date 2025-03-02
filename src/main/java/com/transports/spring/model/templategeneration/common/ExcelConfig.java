package com.transports.spring.model.templategeneration.common;

import com.transports.spring.model.templategeneration.driver.*;
import com.transports.spring.model.templategeneration.passenger.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

@Configuration
public class ExcelConfig {

    @Bean
    @Scope(value = "prototype")
    public PassengerTemplateFile getPassengerTemplateFile(final Calendar calendar, final int templateYear, final int templateMonth) throws IOException {
        calendar.set(templateYear, templateMonth - 1, 1);
        final LocalDate templateDate = LocalDate.of(templateYear, templateMonth, 1);
        final PassengerTemplateExcelBodyGenerator passengerTemplateExcelBodyGenerator = new PassengerTemplateExcelBodyGenerator(calendar, templateDate);
        final PassengerTemplateExcelBody passengerTemplateExcelBody = new PassengerTemplateExcelBody(passengerTemplateExcelBodyGenerator);
        final PassengerTemplateExcel passengerTemplateExcel = new PassengerTemplateExcel(passengerTemplateExcelBody);

        return new PassengerTemplateFile(passengerTemplateExcel);
    }

    @Bean
    @Scope(value = "prototype")
    public DriverTemplateFile getDriverTemplateFile(final Calendar calendar, final int templateYear, final int templateMonth) throws IOException {
        calendar.set(templateYear, templateMonth - 1, 1);
        final LocalDate templateDate = LocalDate.of(templateYear, templateMonth, 1);
        final DriverTemplateExcelBodyGenerator driverTemplateExcelBodyGenerator = new DriverTemplateExcelBodyGenerator(calendar, templateDate);
        final DriverTemplateExcelBody driverTemplateExcelBody = new DriverTemplateExcelBody(driverTemplateExcelBodyGenerator);
        final DriverTemplateExcel driverTemplateExcel = new DriverTemplateExcel(driverTemplateExcelBody);

        return new DriverTemplateFile(driverTemplateExcel);
    }

}