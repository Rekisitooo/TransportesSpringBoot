package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.DtoTemplateFileDir;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.templategeneration.common.EnumTemplateFileDirectory;
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

    public void generateFiles(final DtoGenerateFile dtoGenerateFile, final DtoTemplateExcelHeader dtoHeader) throws IOException, GeneratePdfFromExcelException, GenerateJpgFromExcelException {
        final DtoTemplateFileDir dtoTemplateFileDir = generateTempDirectories(dtoGenerateFile);
        dtoGenerateFile.setDtoTemplateFileDir(dtoTemplateFileDir);

        this.driverTemplateFileGenerator.generateFiles(dtoGenerateFile, dtoHeader);
        this.passengerTemplateFileGenerator.generateFiles(dtoGenerateFile, dtoHeader);
    }

    private static DtoTemplateFileDir generateTempDirectories(final DtoGenerateFile dtoGenerateFile) throws IOException {
        final DtoTemplateFileDir dtoTemplateFileDir = new DtoTemplateFileDir();
        final Path monthTempDirPath = dtoGenerateFile.getMonthTempDirPath();
        final Path passengersTempDir = Files.createTempDirectory(monthTempDirPath, "viajeros");

        final Path excel = Files.createTempDirectory(passengersTempDir, "Excel");
        dtoTemplateFileDir.put(EnumTemplateFileDirectory.EXCEL, excel);

        final Path pdf = Files.createTempDirectory(passengersTempDir, "PDF");
        dtoTemplateFileDir.put(EnumTemplateFileDirectory.PDF, pdf);

        final Path jpg = Files.createTempDirectory(passengersTempDir, "JPG");
        dtoTemplateFileDir.put(EnumTemplateFileDirectory.JPG, jpg);

        return dtoTemplateFileDir;
    }

}
