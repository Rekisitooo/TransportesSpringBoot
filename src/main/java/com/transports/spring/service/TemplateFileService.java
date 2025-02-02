package com.transports.spring.service;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.exception.CreatingTemplateFileException;
import com.transports.spring.model.*;
import com.transports.spring.operation.filesgeneration.TemplateFileGenerator;
import com.transports.spring.service.templatefile.TemplateFileGeneratorService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class TemplateFileService {

    private final MonthService monthService;
    private final TemplateService templateService;
    private final TransportService transportService;
    private final InvolvedByTemplateService involvedByTemplateService;

    private static final Object CONCURRENCY_LOCKER = new Object();

    public TemplateFileService(MonthService monthService, TemplateService templateService, TransportService transportService, InvolvedByTemplateService involvedByTemplateService, TemplateFileGeneratorService templateFileGenerator) {
        this.monthService = monthService;
        this.templateService = templateService;
        this.transportService = transportService;
        this.involvedByTemplateService = involvedByTemplateService;
    }

    public void generateFiles(final int templateId) throws IOException, CreatingTemplateFileException {
        synchronized (CONCURRENCY_LOCKER) {
            final Template template = this.templateService.findById(templateId);
            final int templateYear = getIntFromString(template.getYear());
            final int templateMonth = getIntFromString(template.getMonth());
            final String monthName = getTemplateMonthName(templateMonth);

            final Map<Integer, List<DtoPassengerTransport>> passengerTransports = this.getPassengerTransports(templateId);
            final Map<Integer, List<DtoDriverTransport>> driverTransports = this.getDriverTransports(templateId);

            final Path temporalDirPath = Files.createTempDirectory(templateYear + '_' + monthName + "_transports");
            final TemplateFileGenerator templateFile = new TemplateFileGenerator(temporalDirPath);
            templateFile.generateFiles(passengerTransports, driverTransports);
        }
    }

    private String getTemplateMonthName(int templateMonth) {
        final Month templateMonthObj = this.monthService.findById(templateMonth);
        return templateMonthObj.getName();
    }

    private static int getIntFromString(final String convetToString) {
        return Integer.parseInt(convetToString);
    }

    private Map<Integer, List<DtoPassengerTransport>> getPassengerTransports(int templateId) {
        final Map<Integer, List<DtoPassengerTransport>> passengerTransportsFromTemplateMap = new HashMap<>();
        final List<Passenger> allPassengersFromTemplate = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : allPassengersFromTemplate) {
            final List<DtoPassengerTransport> passengerTransportsFromTemplate = this.transportService.findPassengerTransportsFromTemplate(passenger.getId(), templateId);
            if (!passengerTransportsFromTemplate.isEmpty()) {
                passengerTransportsFromTemplateMap.put(passenger.getId(), passengerTransportsFromTemplate);
            }
        }

        return passengerTransportsFromTemplateMap;
    }

    private Map<Integer, List<DtoDriverTransport>> getDriverTransports(final int templateId) {
        final Map<Integer, List<DtoDriverTransport>> driverTransportsFromTemplateMap = new HashMap<>();
        final List<Driver> allDriversFromTemplate = this.involvedByTemplateService.getAllDriversFromTemplate(templateId);
        for (final Driver driver : allDriversFromTemplate) {
            final List<DtoDriverTransport> driverTransportsFromTemplate = this.transportService.findDriverTransportsFromTemplate(driver.getId(), templateId);
            if (!driverTransportsFromTemplate.isEmpty()) {
                driverTransportsFromTemplateMap.put(driver.getId(), driverTransportsFromTemplate);
            }
        }

        return driverTransportsFromTemplateMap;
    }

}
