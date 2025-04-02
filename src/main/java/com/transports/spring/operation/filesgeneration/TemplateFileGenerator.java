package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        final Path parentTempDir = DirectoryGenerator.generateParentTempDir(dtoHeader);

        this.driverTemplateFileGenerator.generate(dtoGenerateFile, dtoHeader, parentTempDir);
        this.passengerTemplateFileGenerator.generate(dtoGenerateFile, dtoHeader, parentTempDir);
    }

}
