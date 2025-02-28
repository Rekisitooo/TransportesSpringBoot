package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.model.templategeneration.common.AbstractTemplateExcelBodyGenerator;
import com.transports.spring.model.templategeneration.common.cell.DefaultTemplateExcelDayCellGroup;

import com.transports.spring.model.templategeneration.driver.DriverCustomTemplateExcelTransportDayCellGroup;
import org.apache.poi.xssf.usermodel.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassengerTemplateExcelBodyGenerator extends AbstractTemplateExcelBodyGenerator {

    public PassengerTemplateExcelBodyGenerator(final Calendar templateMonthCalendar) {
        super(templateMonthCalendar);
    }

    public void generate(final XSSFSheet excelSheet, final DtoTemplateExcelPassengerBody dtoPassengerBody) {
        final Map<Integer, DtoPassengerTransport> transportDateMap = getTransportForDayMap(dtoPassengerBody);
        final Map<Integer, TransportDateByTemplate> dateMap = getTransportDateMap(dtoPassengerBody);

        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup = new DtoTemplateExcelTransportCellGroup(this.currentCol, this.currentRow, excelSheet, String.valueOf(currentDayOfMonth));

            final TransportDateByTemplate transportDate = dateMap.get(currentDayOfMonth);
            if (transportDate == null) {
                final DefaultTemplateExcelDayCellGroup dayCellGroup = new DefaultTemplateExcelDayCellGroup();
                dayCellGroup.generate(dtoTemplateExcelTransportCellGroup);
            } else {
                final DtoPassengerTransport passengerTransport = transportDateMap.get(currentDayOfMonth);
                if (passengerTransport == null) {
                    dtoTemplateExcelTransportCellGroup.setBodyText("No hay conductores disponibles.");
                } else {
                    dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + passengerTransport.getEventName());
                    dtoTemplateExcelTransportCellGroup.setHeaderText("Te lleva:");
                    dtoTemplateExcelTransportCellGroup.setBodyText(passengerTransport.getDriverFullName());
                }

                final DriverCustomTemplateExcelTransportDayCellGroup transportDayCell = new DriverCustomTemplateExcelTransportDayCellGroup();
                transportDayCell.generate(dtoTemplateExcelTransportCellGroup);
            }

            this.currentCol += 2;
        }
    }

    private static Map<Integer, TransportDateByTemplate> getTransportDateMap(DtoTemplateExcelPassengerBody dtoPassengerBody) {
        final Map<Integer, TransportDateByTemplate> transportDateMap = new HashMap<>();

        final List<TransportDateByTemplate> monthTransportDates = dtoPassengerBody.getMonthTransportDates();
        for (final TransportDateByTemplate transportDateByTemplate : monthTransportDates) {
            final String transportDateString = transportDateByTemplate.getTransportDate();
            final LocalDate transportDate = LocalDate.parse(transportDateString);

            transportDateMap.put(transportDate.getDayOfMonth(), transportDateByTemplate);
        }
        return transportDateMap;
    }

    private static Map<Integer, DtoPassengerTransport> getTransportForDayMap(DtoTemplateExcelPassengerBody dtoPassengerBody) {
        final Map<Integer, DtoPassengerTransport> transportForDayMap = new HashMap<>();

        final List<DtoPassengerTransport> allTemplatePassengerTransports = dtoPassengerBody.getAllTemplateInvolvedTransports();
        for (final DtoPassengerTransport dtoPassengerTransport : allTemplatePassengerTransports) {
            final String transportDateString = dtoPassengerTransport.getTransportDate();
            final LocalDate transportDate = LocalDate.parse(transportDateString);

            transportForDayMap.put(transportDate.getDayOfMonth(), dtoPassengerTransport);
        }
        return transportForDayMap;
    }
}
