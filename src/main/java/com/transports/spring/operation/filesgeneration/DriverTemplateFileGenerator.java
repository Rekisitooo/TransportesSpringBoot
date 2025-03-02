package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.generatefiles.DtoGenerateDriverFile;
import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.DtoTemplateFileDir;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.Driver;
import com.transports.spring.model.templategeneration.driver.DriverTemplateFile;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;

@Component
public final class DriverTemplateFileGenerator {

    private final BeanFactory beanFactory;

    public DriverTemplateFileGenerator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void generateFiles(final DtoGenerateFile dtoGenerateFile, final DtoTemplateExcelHeader dtoHeader) throws IOException, GenerateJpgFromExcelException, GeneratePdfFromExcelException {
        final DtoGenerateDriverFile dtoGenerateDriverFile = dtoGenerateFile.getDtoGenerateDriverFile();

        final Map<Driver, Map<LocalDate, DtoDriverTransport>> driverTransports = dtoGenerateDriverFile.getDriverTransports();
        for (final Map.Entry<Driver, Map<LocalDate, DtoDriverTransport>> entry : driverTransports.entrySet()) {
            final Driver driver = entry.getKey();
            final Map<LocalDate, DtoDriverTransport> dtoDriverTransportList = entry.getValue();

            dtoHeader.setInvolvedFullName(driver.getFullName());
            final Integer templateYear = dtoHeader.getTemplateYear();
            final Calendar monthCalendar = dtoGenerateFile.getMonthCalendar();
            final Integer templateMonth = dtoGenerateFile.getTemplateMonth();
            final DriverTemplateFile driverTemplateFile = (DriverTemplateFile) beanFactory.getBean("getDriverTemplateFile", monthCalendar, templateYear, templateMonth);
            final DtoTemplateExcelDriverBody templateExcelDriverBody = new DtoTemplateExcelDriverBody(dtoDriverTransportList);
            final DtoTemplateFileDir dtoTemplateFileDir = dtoGenerateFile.getDtoTemplateFileDir();
            driverTemplateFile.generate(templateExcelDriverBody, dtoHeader, dtoTemplateFileDir);
        }
    }
}
