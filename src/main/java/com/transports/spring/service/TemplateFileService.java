package com.transports.spring.service;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.model.*;
import com.transports.spring.operation.filesgeneration.TemplateFileGenerator;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final Object CONCURRENCY_LOCKER = new Object();

    public TemplateFileService(MonthService monthService, TemplateService templateService, TransportService transportService, InvolvedByTemplateService involvedByTemplateService, TemplateFileGenerator templateFileGenerator, TransportDateByTemplateService transportDateByTemplateService) {
        this.monthService = monthService;
        this.templateService = templateService;
        this.transportService = transportService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.templateFileGenerator = templateFileGenerator;
        this.transportDateByTemplateService = transportDateByTemplateService;
    }

    public void generateFiles(final int templateId) throws IOException {
        synchronized (CONCURRENCY_LOCKER) {
            final Template template = this.templateService.findById(templateId);
            final int templateYear = getIntFromString(template.getYear());
            final int templateMonth = getIntFromString(template.getMonth());
            final String monthName = getTemplateMonthName(templateMonth);

            final Map<Passenger, List<DtoPassengerTransport>> passengerTransports = this.getPassengerTransports(templateId);
            final List<TransportDateByTemplate> templateMonthDateList = transportDateByTemplateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
            final Map<Driver, List<DtoDriverTransport>> driverTransports = this.getDriverTransports(templateId);
            final Path monthTempDirPath = Files.createTempDirectory(templateYear + '_' + monthName + "_transports");

            this.templateFileGenerator.generateFiles(new DtoGenerateFile(passengerTransports, driverTransports), new DtoTemplateExcelHeader(monthName, templateYear), templateMonth, monthTempDirPath, templateMonthDateList);
        }
    }

    private String getTemplateMonthName(int templateMonth) {
        final Month templateMonthObj = this.monthService.findById(templateMonth);
        return templateMonthObj.getName();
    }

    private static int getIntFromString(final String convetToString) {
        return Integer.parseInt(convetToString);
    }

    private Map<Passenger, List<DtoPassengerTransport>> getPassengerTransports(int templateId) {
        final Map<Passenger, List<DtoPassengerTransport>> passengerTransportsFromTemplateMap = new HashMap<>();

        final List<Passenger> allPassengersFromTemplate = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : allPassengersFromTemplate) {
            final List<DtoPassengerTransport> passengerTransportsFromTemplate = this.transportService.findPassengerTransportsFromTemplate(passenger.getId(), templateId);
            if (!passengerTransportsFromTemplate.isEmpty()) {
                passengerTransportsFromTemplateMap.put(passenger, passengerTransportsFromTemplate);
            }
        }

        return passengerTransportsFromTemplateMap;
    }

    private Map<Driver, List<DtoDriverTransport>> getDriverTransports(final int templateId) {
        final Map<Driver, List<DtoDriverTransport>> driverTransportsFromTemplateMap = new HashMap<>();
        final List<Driver> allDriversFromTemplate = this.involvedByTemplateService.getAllDriversFromTemplate(templateId);
        for (final Driver driver : allDriversFromTemplate) {
            final List<DtoDriverTransport> driverTransportsFromTemplate = this.transportService.findDriverTransportsFromTemplate(driver.getId(), templateId);
            if (!driverTransportsFromTemplate.isEmpty()) {
                driverTransportsFromTemplateMap.put(driver, driverTransportsFromTemplate);
            }
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
