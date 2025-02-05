package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.templategeneration.PassengerTemplateFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class TemplateFileGenerator {

    private final Path temporalDirPath;
    private final PassengerTemplateFile passengerTemplateFile;
    //private final DriverTemplateFile driverTemplateFile;

    public TemplateFileGenerator(Path temporalDirPath, int templateYear, int templateMonth) throws IOException {
        this.temporalDirPath = temporalDirPath;
        this.passengerTemplateFile = new PassengerTemplateFile(templateYear, templateMonth);
        //this.driverTemplateFile = new DriverTemplateFile();
    }

    public void generateFiles(final Map<Passenger, List<DtoInvolvedTransport>> passengerTransports, final Map<Driver, List<DtoDriverTransport>> driverTransports, final String monthName, final int templateYear) throws IOException {
        final Path passengersTempDir = Files.createTempDirectory(this.temporalDirPath, "viajeros");
        generateFileTempDirectories(passengersTempDir);
        for (final Map.Entry<Passenger, List<DtoInvolvedTransport>> entry : passengerTransports.entrySet()) {
            final Passenger passenger = entry.getKey();
            final List<DtoInvolvedTransport> dtoPassengerTransportList = entry.getValue();
            this.passengerTemplateFile.generate(dtoPassengerTransportList, passenger.getFullName(), monthName, templateYear);
        }

        //final Path driversTempDir = Files.createTempDirectory(this.temporalDirPath, "conductores");
        //generateFilesDirectories(driversTempDir);
        //for (Map.Entry<Driver, List<DtoDriverTransport>> entry : driverTransports.entrySet()) {
        //    final Driver driver = entry.getKey();
        //    final List<DtoDriverTransport> dtoDriverTransportList = entry.getValue();
        //    this.driverTemplateFile.generate(dtoDriverTransportList, driver.getFullName(), monthName, templateYear);
        //}
    }

    private static void generateFileTempDirectories(final Path pathToCreateDirs) throws IOException {
        Files.createTempDirectory(pathToCreateDirs, "excel");
        Files.createTempDirectory(pathToCreateDirs, "jpg");
    }

}
