package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.operation.filesgeneration.driver.DriverTemplateFileGenerator;
import com.transports.spring.operation.filesgeneration.passenger.PassengerTemplateFileGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class TemplateFileGenerator {

    private final Path temporalDirPath;
    private final PassengerTemplateFileGenerator passengerTemplateFileGenerator;
    private final DriverTemplateFileGenerator driverTemplateFile;

    public TemplateFileGenerator(Path temporalDirPath, PassengerTemplateFileGenerator passengerTemplateFileGenerator, DriverTemplateFileGenerator driverTemplateFile) {
        this.temporalDirPath = temporalDirPath;
        this.passengerTemplateFileGenerator = passengerTemplateFileGenerator;
        this.driverTemplateFile = driverTemplateFile;
    }

    public void generateFiles(final Map<Integer, List<DtoPassengerTransport>> passengerTransports, final Map<Integer, List<DtoDriverTransport>> driverTransports) throws IOException {
        final Path passengersTempDir = Files.createTempDirectory(this.temporalDirPath, "viajeros");
        generateFilesDirectories(passengersTempDir);
        for (Map.Entry<Integer, List<DtoPassengerTransport>> entry : passengerTransports.entrySet()) {
            this.passengerTemplateFileGenerator.generateFiles();
        }

        final Path driversTempDir = Files.createTempDirectory(this.temporalDirPath, "conductores");
        generateFilesDirectories(driversTempDir);
        for (Map.Entry<Integer, List<DtoDriverTransport>> driverTransports : driverTransports.entrySet()) {
            this.passengerTemplateFileGenerator.generateFiles();
        }
    }

    private static void generateFilesDirectories(Path passengersTempDir) throws IOException {
        Files.createTempDirectory(passengersTempDir, "excel");
        Files.createTempDirectory(passengersTempDir, "jpg");
    }

}
