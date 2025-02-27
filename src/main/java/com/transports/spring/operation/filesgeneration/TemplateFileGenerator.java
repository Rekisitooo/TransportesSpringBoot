package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.model.TransportDateByTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

@Component
public final class TemplateFileGenerator {

    private final DriverTemplateFileGenerator driverTemplateFileGenerator;
    private final PassengerTemplateFileGenerator passengerTemplateFileGenerator;

    public TemplateFileGenerator(DriverTemplateFileGenerator driverTemplateFileGenerator, PassengerTemplateFileGenerator passengerTemplateFileGenerator) {
        this.driverTemplateFileGenerator = driverTemplateFileGenerator;
        this.passengerTemplateFileGenerator = passengerTemplateFileGenerator;
    }

    public void generateFiles(final DtoGenerateFile dtoGenerateFile, final DtoTemplateExcelHeader dtoHeader, final int templateMonth, final Path monthTempDirPath, final List<TransportDateByTemplate> monthTransportDatesList) throws IOException {
        generateTempDirectories(monthTempDirPath);
        final Calendar calendar = Calendar.getInstance();

        driverTemplateFileGenerator.generateFiles(dtoGenerateFile.getDriverTransports(), dtoHeader, calendar, templateMonth);
        passengerTemplateFileGenerator.generateFiles(dtoGenerateFile.getPassengerTransports(), dtoHeader, calendar, templateMonth, monthTransportDatesList);
    }

    private static void generateTempDirectories(final Path temporalDirPath) throws IOException {
        final Path passengersTempDir = Files.createTempDirectory(temporalDirPath, "viajeros");
        Files.createTempDirectory(passengersTempDir, "Excel");
        Files.createTempDirectory(passengersTempDir, "JPG");
    }

}
