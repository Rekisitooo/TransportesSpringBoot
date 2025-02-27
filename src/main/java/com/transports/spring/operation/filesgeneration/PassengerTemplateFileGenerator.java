package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.model.templategeneration.passenger.PassengerTemplateFile;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Component
public final class PassengerTemplateFileGenerator {

    private final BeanFactory beanFactory;

    public PassengerTemplateFileGenerator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void generateFiles(final Map<Passenger, List<DtoPassengerTransport>> passengerTransports, final DtoTemplateExcelHeader dtoHeader, final Calendar calendar, final int templateMonth, final List<TransportDateByTemplate> monthTransportDatesList) throws IOException {
        for (final Map.Entry<Passenger, List<DtoPassengerTransport>> entry : passengerTransports.entrySet()) {
            final Passenger passenger = entry.getKey();
            final List<DtoPassengerTransport> dtoPassengerTransportList = entry.getValue();

            dtoHeader.setInvolvedFullName(passenger.getFullName());
            final Integer templateYear = dtoHeader.getTemplateYear();
            final PassengerTemplateFile passengerTemplateFile = (PassengerTemplateFile) beanFactory.getBean("getPassengerTemplateFile", calendar, templateYear, templateMonth);
            passengerTemplateFile.generate(new DtoTemplateExcelPassengerBody(monthTransportDatesList, dtoPassengerTransportList), dtoHeader);
            break;
        }
    }
}
