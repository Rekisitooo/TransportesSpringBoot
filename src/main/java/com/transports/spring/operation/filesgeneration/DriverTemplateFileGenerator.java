package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.generatefiles.DtoGenerateDriverFile;
import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.DtoTemplateFileDir;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Event;
import com.transports.spring.model.templategeneration.driver.DriverTemplateFile;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;

@Component
public final class DriverTemplateFileGenerator {

    private final BeanFactory beanFactory;

    public DriverTemplateFileGenerator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void generate(final DtoGenerateFile dtoGenerateFile, final DtoTemplateExcelHeader dtoHeader, final Path parentTempDir) throws IOException, GenerateJpgFromExcelException, GeneratePdfFromExcelException {
        final DtoTemplateFileDir dtoTemplateFileDir = generateDirs(parentTempDir);
        this.generateFiles(dtoGenerateFile, dtoHeader, dtoTemplateFileDir);
    }

    private static DtoTemplateFileDir generateDirs (final Path parentTempDir) throws IOException {
        return DirectoryGenerator.generateTempDirectories(parentTempDir, "Conductores");
    }

    private void generateFiles(final DtoGenerateFile dtoGenerateFile, final DtoTemplateExcelHeader dtoHeader, final DtoTemplateFileDir dtoTemplateFileDir) throws IOException, GenerateJpgFromExcelException, GeneratePdfFromExcelException {
        final DtoGenerateDriverFile dtoGenerateDriverFile = dtoGenerateFile.getDtoGenerateDriverFile();

        final Map<Driver, Map<LocalDate, DtoDriverTransport>> driverTransports = dtoGenerateDriverFile.getDriverTransports();
        for (final Map.Entry<Driver, Map<LocalDate, DtoDriverTransport>> entry : driverTransports.entrySet()) {
            final Driver driver = entry.getKey();
            final Map<LocalDate, DtoDriverTransport> dtoDriverTransportList = entry.getValue();

            dtoHeader.setInvolvedFullName(driver.getFullName());
            final Integer templateYear = dtoHeader.getTemplateYear();
            final Calendar monthCalendar = dtoGenerateFile.getMonthCalendar();
            final Integer templateMonth = dtoGenerateFile.getTemplateMonth();
            final DriverTemplateFile driverTemplateFile = (DriverTemplateFile) this.beanFactory.getBean(
                    "getDriverTemplateFile", monthCalendar, templateYear, templateMonth);

            final Map<LocalDate, Event> dateEventMap = dtoGenerateFile.getDateEventMap();
            final DtoTemplateExcelDriverBody templateExcelDriverBody = new DtoTemplateExcelDriverBody(dtoDriverTransportList, dateEventMap);
            driverTemplateFile.generate(templateExcelDriverBody, dtoHeader, dtoTemplateFileDir);
        }
    }
}
