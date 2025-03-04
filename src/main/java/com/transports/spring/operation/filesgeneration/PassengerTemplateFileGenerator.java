package com.transports.spring.operation.filesgeneration;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.dto.DtoTransportDateByTemplate;
import com.transports.spring.dto.generatefiles.DtoGenerateFile;
import com.transports.spring.dto.generatefiles.DtoGeneratePassengerFile;
import com.transports.spring.dto.generatefiles.DtoTemplateFileDir;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.Event;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.templategeneration.passenger.PassengerTemplateFile;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Component
public final class PassengerTemplateFileGenerator {

    private final BeanFactory beanFactory;

    public PassengerTemplateFileGenerator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void generate(final DtoGenerateFile dtoGenerateFile, final DtoTemplateExcelHeader dtoHeader, final Path parentTempDir) throws IOException, GenerateJpgFromExcelException, GeneratePdfFromExcelException {
        final DtoTemplateFileDir dtoTemplateFileDir = generateDirs(parentTempDir);
        this.generateFiles(dtoGenerateFile, dtoHeader, dtoTemplateFileDir);
    }

    private static DtoTemplateFileDir generateDirs (final Path parentTempDir) throws IOException {
        return DirectoryGenerator.generateTempDirectories(parentTempDir, "Viajeros");
    }

    private void generateFiles(final DtoGenerateFile dtoGenerateFile, final DtoTemplateExcelHeader dtoHeader, final DtoTemplateFileDir dtoTemplateFileDir) throws IOException, GeneratePdfFromExcelException, GenerateJpgFromExcelException {
        final DtoGeneratePassengerFile dtoGeneratePassengerFile = dtoGenerateFile.getDtoGeneratePassengerFile();
        final Map<Passenger, Map<LocalDate, DtoPassengerTransport>> passengerTransports = dtoGeneratePassengerFile.getPassengerTransports();

        for (final Map.Entry<Passenger, Map<LocalDate, DtoPassengerTransport>> entry : passengerTransports.entrySet()) {
            final Passenger passenger = entry.getKey();
            final Map<LocalDate, DtoPassengerTransport> passengerTransportsByDayMap = entry.getValue();

            dtoHeader.setInvolvedFullName(passenger.getFullName());
            final Integer templateYear = dtoHeader.getTemplateYear();
            final Calendar monthCalendar = dtoGenerateFile.getMonthCalendar();
            final Integer templateMonth = dtoGenerateFile.getTemplateMonth();
            final PassengerTemplateFile passengerTemplateFile = (PassengerTemplateFile) this.beanFactory.getBean(
                    "getPassengerTemplateFile", monthCalendar, templateYear, templateMonth);

            final Map<LocalDate, Event> dateEventMap = dtoGenerateFile.getDateEventMap();
            final DtoTemplateExcelPassengerBody dtoTemplateExcelPassengerBody = getDtoTemplateExcelPassengerBody(dtoGeneratePassengerFile, passenger, passengerTransportsByDayMap, dateEventMap);
            passengerTemplateFile.generate(dtoTemplateExcelPassengerBody, dtoHeader, dtoTemplateFileDir);
        }
    }

    private static DtoTemplateExcelPassengerBody getDtoTemplateExcelPassengerBody(final DtoGeneratePassengerFile dtoGeneratePassengerFile,
              final Passenger passenger, final Map<LocalDate, DtoPassengerTransport> passengerTransportsByDayMap, final Map<LocalDate, Event> dateEventMap) {

        final Map<Integer, List<DtoTemplateDay>> allPassengersAssistanceDatesMap = dtoGeneratePassengerFile.getAllPassengersAssistanceDatesMap();
        final List<DtoTemplateDay> passengerAssistanceDatesMap = allPassengersAssistanceDatesMap.get(passenger.getId());
        final Map<LocalDate, DtoTransportDateByTemplate> monthTransportDatesList = dtoGeneratePassengerFile.getMonthTransportDatesList();

        return new DtoTemplateExcelPassengerBody(passengerAssistanceDatesMap, monthTransportDatesList, passengerTransportsByDayMap, dateEventMap);
    }
}
