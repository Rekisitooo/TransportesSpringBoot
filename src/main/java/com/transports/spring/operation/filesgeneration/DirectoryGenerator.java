package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.generatefiles.DtoTemplateFileDir;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.model.templategeneration.common.EnumTemplateFileDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public final class DirectoryGenerator {

    private DirectoryGenerator() {}

    public static Path generateParentTempDir(final DtoTemplateExcelHeader dtoHeader) throws IOException {
        final String monthName = dtoHeader.getMonthName();
        final Integer templateYear = dtoHeader.getTemplateYear();
        return Files.createTempDirectory("Transportes_" + templateYear + '_' + monthName);
    }

    public static DtoTemplateFileDir generateTempDirectories(final Path monthTempDirPath, final String involvedEntityName) throws IOException {
        final Path driversDir = Path.of(monthTempDirPath + "/" + involvedEntityName);
        final Path driversTempDir = Files.createDirectory(driversDir);
        return generateInternalDirectories(driversTempDir);
    }

    private static DtoTemplateFileDir generateInternalDirectories(final Path tempDir) throws IOException {
        final DtoTemplateFileDir dtoTemplateFileDir = new DtoTemplateFileDir();

        Path excelDir = createDir(tempDir, EnumTemplateFileDirectory.EXCEL);
        dtoTemplateFileDir.put(EnumTemplateFileDirectory.EXCEL, excelDir);

        Path pdfDir = createDir(tempDir, EnumTemplateFileDirectory.PDF);
        dtoTemplateFileDir.put(EnumTemplateFileDirectory.PDF, pdfDir);

        Path jpgDir = createDir(tempDir, EnumTemplateFileDirectory.JPG);
        dtoTemplateFileDir.put(EnumTemplateFileDirectory.JPG, jpgDir);

        return dtoTemplateFileDir;
    }

    private static Path createDir(final Path tempDir, final EnumTemplateFileDirectory enumTemplateFileDirectory) throws IOException {
        final String directoryName = enumTemplateFileDirectory.toString();
        final Path dirPath = Path.of(tempDir + "/" + directoryName);
        return Files.createDirectory(dirPath);
    }

}
