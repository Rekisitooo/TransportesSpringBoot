package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.model.Driver;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.model.templategeneration.driver.DriverTemplateFile;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Component
public final class DriverTemplateFileGenerator {

    private final BeanFactory beanFactory;

    public DriverTemplateFileGenerator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void generateFiles(final Map<Driver, List<DtoDriverTransport>> driverTransports, final DtoTemplateExcelHeader dtoHeader, final Calendar calendar, final int templateMonth) throws IOException {
        for (final Map.Entry<Driver, List<DtoDriverTransport>> entry : driverTransports.entrySet()) {
            final Driver driver = entry.getKey();
            final List<DtoDriverTransport> dtoPassengerTransportList = entry.getValue();

            dtoHeader.setInvolvedFullName(driver.getFullName());
            final Integer templateYear = dtoHeader.getTemplateYear();
            final DriverTemplateFile driverTemplateFile = (DriverTemplateFile) beanFactory.getBean("getDriverTemplateFile", calendar, templateYear, templateMonth);
            driverTemplateFile.generate(new DtoTemplateExcelDriverBody(dtoPassengerTransportList), dtoHeader);
            break;
        }
    }
}
