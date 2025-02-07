package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.templategeneration.PassengerTemplateFile;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Component
public final class TemplateFileGenerator {

    private final BeanFactory beanFactory;

    public TemplateFileGenerator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void generateFiles(final Map<Passenger, List<DtoInvolvedTransport>> passengerTransports, final Map<Driver, List<DtoDriverTransport>> driverTransports, final String monthName, final int templateYear, final int templateMonth, final Path monthTempDirPath) throws IOException {
        generateTempDirectories(monthTempDirPath);

        final Calendar calendar = Calendar.getInstance();
        for (final Map.Entry<Passenger, List<DtoInvolvedTransport>> entry : passengerTransports.entrySet()) {
            final PassengerTemplateFile passengerTemplateFile = (PassengerTemplateFile) beanFactory.getBean("getPassengerTemplateFile", calendar, templateYear, templateMonth);
            final Passenger passenger = entry.getKey();
            final List<DtoInvolvedTransport> dtoPassengerTransportList = entry.getValue();
            passengerTemplateFile.generate(dtoPassengerTransportList, passenger.getFullName(), monthName, templateYear);
            break;
        }

        //final Path driversTempDir = Files.createTempDirectory(this.temporalDirPath, "conductores");
        //generateFilesDirectories(driversTempDir);
        //for (Map.Entry<Driver, List<DtoDriverTransport>> entry : driverTransports.entrySet()) {
        //    final Driver driver = entry.getKey();
        //    final List<DtoDriverTransport> dtoDriverTransportList = entry.getValue();
        //    this.driverTemplateFile.generate(dtoDriverTransportList, driver.getFullName(), monthName, templateYear);
        //}
    }

    private static void generateTempDirectories(final Path temporalDirPath) throws IOException {
        final Path passengersTempDir = Files.createTempDirectory(temporalDirPath, "viajeros");
        Files.createTempDirectory(passengersTempDir, "Excel");
        Files.createTempDirectory(passengersTempDir, "JPG");
    }

}
