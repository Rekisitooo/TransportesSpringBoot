package com.transports.spring.service;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.dto.generatefiles.DtoGenerateDriverFile;
import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.DtoGeneratePassengerFile;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.*;
import com.transports.spring.operation.filesgeneration.TemplateFileGenerator;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public final class TemplateFileService {

    private final MonthService monthService;
    private final TemplateService templateService;
    private final TransportService transportService;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TemplateFileGenerator templateFileGenerator;
    private final TransportDateByTemplateService transportDateByTemplateService;
    private final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;
    private final EventService eventService;

    private static final Object CONCURRENCY_LOCKER = new Object();

    public TemplateFileService(MonthService monthService, TemplateService templateService, TransportService transportService, InvolvedByTemplateService involvedByTemplateService, TemplateFileGenerator templateFileGenerator, TransportDateByTemplateService transportDateByTemplateService, InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService, EventService eventService) {
        this.monthService = monthService;
        this.templateService = templateService;
        this.transportService = transportService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.templateFileGenerator = templateFileGenerator;
        this.transportDateByTemplateService = transportDateByTemplateService;
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
        this.eventService = eventService;
    }

    public void generateFiles(final int templateId) throws IOException, GeneratePdfFromExcelException, GenerateJpgFromExcelException {
        synchronized (CONCURRENCY_LOCKER) {
            final Template template = this.templateService.findById(templateId);
            final int templateYear = getIntFromString(template.getYear());
            final int templateMonth = getIntFromString(template.getMonth());
            final String monthName = this.getTemplateMonthName(templateMonth);

            final Map<Passenger, Map<LocalDate, DtoPassengerTransport>> passengerTransports = this.getPassengerTransports(templateId);
            final Map<LocalDate, DtoTemplateDate> templateMonthDateByDayMap = this.transportDateByTemplateService.getTransportDateByDayMap(templateId);
            final Map<Integer, Map<LocalDate, DtoTemplateDay>> allPassengersAssistanceDatesMap = this.involvedAvailabiltyForTransportDateService.findAllPassengersAssistanceDates(templateId);

            final Map<Driver, Map<LocalDate, DtoDriverTransport>> driverTransports = this.getDriverTransports(templateId);

            final DtoGeneratePassengerFile dtoGeneratePassengerFile = new DtoGeneratePassengerFile(passengerTransports, templateMonthDateByDayMap, allPassengersAssistanceDatesMap);
            final DtoGenerateDriverFile dtoGenerateDriverFile = new DtoGenerateDriverFile(driverTransports);
            final DtoGenerateFile dtoGenerateFile = new DtoGenerateFile(dtoGeneratePassengerFile, dtoGenerateDriverFile, templateMonth, Calendar.getInstance());
            final DtoTemplateExcelHeader dtoHeader = new DtoTemplateExcelHeader(monthName, templateYear);
            this.templateFileGenerator.generateFiles(dtoGenerateFile, dtoHeader);
        }
    }

    private String getTemplateMonthName(int templateMonth) {
        final Month templateMonthObj = this.monthService.findById(templateMonth);
        return templateMonthObj.getName();
    }

    private static int getIntFromString(final String convetToString) {
        return Integer.parseInt(convetToString);
    }

    private Map<Passenger, Map<LocalDate, DtoPassengerTransport>> getPassengerTransports(final int templateId) {
        final Map<Passenger, Map<LocalDate, DtoPassengerTransport>> passengerTransportsFromTemplateMap = new LinkedHashMap<>();

        final List<Passenger> allPassengersFromTemplate = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : allPassengersFromTemplate) {
            final List<DtoPassengerTransport> passengerTransportsFromTemplate = this.transportService.findPassengerTransportsFromTemplate(passenger.getId(), templateId);
            if (!passengerTransportsFromTemplate.isEmpty()) {

                final Map<LocalDate, DtoPassengerTransport> passengerTransportByDateMap = new LinkedHashMap<>();
                for (final DtoPassengerTransport dtoPassengerTransport : passengerTransportsFromTemplate) {
                    final LocalDate transportDate = dtoPassengerTransport.getTransportDate().toLocalDate();
                    passengerTransportByDateMap.put(transportDate, dtoPassengerTransport);
                }
                passengerTransportsFromTemplateMap.put(passenger, passengerTransportByDateMap);
            }
        }

        return passengerTransportsFromTemplateMap;
    }

    private Map<Driver, Map<LocalDate, DtoDriverTransport>> getDriverTransports(final int templateId) {
        final Map<Driver, Map<LocalDate, DtoDriverTransport>> driverTransportsFromTemplateMap = new LinkedHashMap<>();
        final List<Driver> allDriversFromTemplate = this.involvedByTemplateService.getAllDriversFromTemplate(templateId);

        for (final Driver driver : allDriversFromTemplate) {
            final Map<LocalDate, DtoDriverTransport> driverTransportsByDateMap = new LinkedHashMap<>();
            final List<DtoDriverTransport> driverTransportsFromTemplate = this.transportService.findDriverTransportsFromTemplate(driver.getId(), templateId);

            for (final DtoDriverTransport dtoDriverTransport : driverTransportsFromTemplate) {
                final java.sql.Date transportDate = dtoDriverTransport.getTransportDate();
                final LocalDate transportLocalDate = transportDate.toLocalDate();
                driverTransportsByDateMap.put(transportLocalDate, dtoDriverTransport);
            }

            driverTransportsFromTemplateMap.put(driver, driverTransportsByDateMap);
        }

        return driverTransportsFromTemplateMap;
    }

    public void zip() throws IOException {
        final String file1 = "src/main/resources/zipTest/test1.txt";
        final String file2 = "src/main/resources/zipTest/test2.txt";
        final List<String> srcFiles = Arrays.asList(file1, file2);

        final FileOutputStream fos = new FileOutputStream(Paths.get(file1).getParent().toAbsolutePath() + "/compressed.zip");
        final ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (final String srcFile : srcFiles) {
            final File fileToZip = new File(srcFile);
            final FileInputStream fis = new FileInputStream(fileToZip);
            final ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();
    }
}
