package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Component
public final class TemplateFileGenerator {

    private final DriverTemplateFileGenerator driverTemplateFileGenerator;
    private final PassengerTemplateFileGenerator passengerTemplateFileGenerator;

    public TemplateFileGenerator(DriverTemplateFileGenerator driverTemplateFileGenerator, PassengerTemplateFileGenerator passengerTemplateFileGenerator) {
        this.driverTemplateFileGenerator = driverTemplateFileGenerator;
        this.passengerTemplateFileGenerator = passengerTemplateFileGenerator;
    }

    public void generateFiles(final DtoGenerateFile dtoGenerateFile, final DtoTemplateExcelHeader dtoHeader) throws IOException {
        generateTempDirectories(dtoGenerateFile);
        driverTemplateFileGenerator.generateFiles(dtoGenerateFile, dtoHeader);
        passengerTemplateFileGenerator.generateFiles(dtoGenerateFile, dtoHeader);
    }

    private static void generateTempDirectories(final DtoGenerateFile dtoGenerateFile) throws IOException {
        final Path monthTempDirPath = dtoGenerateFile.getMonthTempDirPath();
        final Path passengersTempDir = Files.createTempDirectory(monthTempDirPath, "viajeros");
        Files.createTempDirectory(passengersTempDir, "Excel");
        Files.createTempDirectory(passengersTempDir, "JPG");
    }

}
