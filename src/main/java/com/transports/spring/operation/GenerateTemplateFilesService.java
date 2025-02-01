package com.transports.spring.operation;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.exception.CreatingTemplateFileException;
import com.transports.spring.model.*;
import com.transports.spring.service.InvolvedByTemplateService;
import com.transports.spring.service.MonthService;
import com.transports.spring.service.TemplateService;
import com.transports.spring.service.TransportService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public final class GenerateTemplateFilesService {

    public static final int START_ROW_DAYS = 3;
    public static final int START_COL_DAYS = 0;
    private static final String CALENDAR_NAME_FILE = "calendar";
    private static final String XSL_EXTENSION = ".xlxs";
    private static final String SEPARATION_CHARACTER = "_";
    public static final int MONDAYS_COL = 0;
    public static final int SUNDAYS_COL = 6;
    public static final int OUT_OF_DAYS_COLUM_SCOPE = 7;

    private final MonthService monthService;
    private final TemplateService templateService;
    private final TransportService transportService;
    private final InvolvedByTemplateService involvedByTemplateService;

    public GenerateTemplateFilesService(MonthService monthService, TemplateService templateService, TransportService transportService, InvolvedByTemplateService involvedByTemplateService) {
        this.monthService = monthService;
        this.templateService = templateService;
        this.transportService = transportService;
        this.involvedByTemplateService = involvedByTemplateService;
    }

    public void generateFiles(final int templateId) throws IOException, CreatingTemplateFileException {
        final Template template = this.templateService.findById(templateId);

        final String year = template.getYear();
        final int templateYear = Integer.parseInt(year);
        final String month = template.getMonth();
        final int templateMonth = Integer.parseInt(month);
        final Month templateMonthObj = this.monthService.findById(templateMonth);
        final String monthName = templateMonthObj.getName();

        //first, passengers
        final List<Passenger> passengerList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : passengerList) {
            final List<DtoPassengerTransport> allPassengerTransportsFromTemplate = this.transportService.findPassengerTransportsFromTemplate(passenger.getId(), templateId);
            PassengerTemplateFile.generate(allPassengerTransportsFromTemplate, monthName, templateYear, templateMonth, passenger.getFullName());
        }

        //then, drivers
        final List<Driver> allDriversFromTemplate = this.involvedByTemplateService.getAllDriversFromTemplate(templateId);
        for (final Driver driver : allDriversFromTemplate) {
            final List<DtoDriverTransport> driverTransportsFromTemplate = this.transportService.findDriverTransportsFromTemplate(driver.getId(), templateId);
            DriverTemplateFile.generate(driverTransportsFromTemplate, monthName, templateYear, templateMonth, driver.getFullName());
        }

    }
}