package com.transports.spring.model.templategeneration;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.Calendar;

@Configuration
public class ExcelConfig {

    @Bean
    @Scope(value = "prototype")
    public PassengerTemplateFile getPassengerTemplateFile(final Calendar calendar, final int templateYear, final int templateMonth) throws IOException {
        calendar.set(templateYear, templateMonth - 1, 1);
        final PassengerTemplateExcelBodyGenerator passengerTemplateExcelBodyGenerator = new PassengerTemplateExcelBodyGenerator(calendar);
        final PassengerTemplateExcelBody passengerTemplateExcelBody = new PassengerTemplateExcelBody(passengerTemplateExcelBodyGenerator);
        final PassengerTemplateExcel passengerTemplateExcel = new PassengerTemplateExcel(passengerTemplateExcelBody);

        return new PassengerTemplateFile(passengerTemplateExcel, new PassengerTemplateJpg());
    }


}