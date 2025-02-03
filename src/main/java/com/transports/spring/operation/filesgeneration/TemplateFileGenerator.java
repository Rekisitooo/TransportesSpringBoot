package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.templategeneration.PassengerTemplateFile;
import com.transports.spring.operation.filesgeneration.driver.DriverTemplateFileGenerator;
import com.transports.spring.operation.filesgeneration.passenger.PassengerTemplateFileGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class TemplateFileGenerator {

    private final Path temporalDirPath;
    private final PassengerTemplateFile passengerTemplateFile;
    private final DriverTemplateFile driverTemplateFile;

    public TemplateFileGenerator(Path temporalDirPath) throws IOException {
        this.temporalDirPath = temporalDirPath;
        this.passengerTemplateFile = new PassengerTemplateFile();
        this.driverTemplateFile = new DriverTemplateFile();
    }

    public void generateFiles(final Map<Passenger, List<DtoPassengerTransport>> passengerTransports, final Map<Driver, List<DtoDriverTransport>> driverTransports, final String monthName, final int templateYear) throws IOException {
        final Path passengersTempDir = Files.createTempDirectory(this.temporalDirPath, "viajeros");
        generateFilesDirectories(passengersTempDir);
        for (Map.Entry<Passenger, List<DtoPassengerTransport>> entry : passengerTransports.entrySet()) {
            final Passenger passenger = entry.getKey();
            final List<DtoPassengerTransport> dtoPassengerTransportList = entry.getValue();
            this.passengerTemplateFile.generate(dtoPassengerTransportList, passenger.getFullName(), monthName, templateYear);
        }

        final Path driversTempDir = Files.createTempDirectory(this.temporalDirPath, "conductores");
        generateFilesDirectories(driversTempDir);
        for (Map.Entry<Driver, List<DtoDriverTransport>> entry : driverTransports.entrySet()) {
            final Driver driver = entry.getKey();
            final List<DtoDriverTransport> dtoDriverTransportList = entry.getValue();
            this.driverTemplateFile.generate(dtoDriverTransportList, driver.getFullName(), monthName, templateYear);
        }
    }

    private static void generateFilesDirectories(Path passengersTempDir) throws IOException {
        Files.createTempDirectory(passengersTempDir, "excel");
        Files.createTempDirectory(passengersTempDir, "jpg");
    }

}
