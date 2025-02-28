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
import java.util.*;

public class PassengerTemplateExcelBodyGenerator extends AbstractTemplateExcelBodyGenerator {

    public PassengerTemplateExcelBodyGenerator(final Calendar templateMonthCalendar, final LocalDate templateDate) {
        super(templateMonthCalendar, templateDate);
    }

    public void generate(final XSSFSheet excelSheet, final DtoTemplateExcelPassengerBody dtoPassengerBody) {
        final Map<LocalDate, DtoPassengerTransport> passengerTransportDateMap = dtoPassengerBody.getAllTemplatePassengerTransportsByDayMap();
        final Map<LocalDate, TransportDateByTemplate> dateMap = dtoPassengerBody.getMonthTransportDateByDayMap();
        final List<LocalDate> passengerAssistanceDates = dtoPassengerBody.getPassengerAssistanceDateList();

        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup = new DtoTemplateExcelTransportCellGroup(this.currentCol, this.currentRow, excelSheet, String.valueOf(currentDayOfMonth));

            final TransportDateByTemplate templateTransportDate = dateMap.get(super.templateDate);
            if (isActualDateTransportDate(templateTransportDate)) {

                final DtoPassengerTransport passengerTransport = passengerTransportDateMap.get(super.templateDate);
                if (isPassengerArrangedInTransportInActualDate(passengerTransport)) {
                    dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + passengerTransport.getEventName());
                    dtoTemplateExcelTransportCellGroup.setHeaderText("Te lleva:");
                    dtoTemplateExcelTransportCellGroup.setBodyText(passengerTransport.getDriverFullName());

                    generateCustomTemplateExcelTransportDayCellGroup(dtoTemplateExcelTransportCellGroup);

                } else {
                    if (isPassengerAssistingOnActualDate(passengerAssistanceDates)){
                        dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + passengerTransport.getEventName());
                        dtoTemplateExcelTransportCellGroup.setBodyText("No hay conductores disponibles.");
                        generateCustomTemplateExcelTransportDayCellGroup(dtoTemplateExcelTransportCellGroup);
                    } else {
                        generateDefaultTemplateExcelDayGroup(dtoTemplateExcelTransportCellGroup);
                    }
                }
            } else {
                generateDefaultTemplateExcelDayGroup(dtoTemplateExcelTransportCellGroup);
            }

            this.currentCol += 2;
            super.templateDate = super.templateDate.plusDays(1);
        }
    }

    private static void generateCustomTemplateExcelTransportDayCellGroup(DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup) {
        final DriverCustomTemplateExcelTransportDayCellGroup transportDayCell = new DriverCustomTemplateExcelTransportDayCellGroup();
        transportDayCell.generate(dtoTemplateExcelTransportCellGroup);
    }

    private static void generateDefaultTemplateExcelDayGroup(DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup) {
        final DefaultTemplateExcelDayCellGroup dayCellGroup = new DefaultTemplateExcelDayCellGroup();
        dayCellGroup.generate(dtoTemplateExcelTransportCellGroup);
    }

    private boolean isPassengerAssistingOnActualDate(List<LocalDate> passengerAssistanceDates) {
        boolean isPassengerAssistingOnActualDate = false;
        for (final LocalDate passengerAssistanceDate : passengerAssistanceDates) {
            if (passengerAssistanceDate.isEqual(super.templateDate)) {
                isPassengerAssistingOnActualDate = true;
                break;
            }
        }

        return isPassengerAssistingOnActualDate;
    }

    private static boolean isPassengerArrangedInTransportInActualDate(DtoPassengerTransport passengerTransport) {
        return passengerTransport != null;
    }

    private static boolean isActualDateTransportDate(TransportDateByTemplate templateTransportDate) {
        return templateTransportDate != null;
    }
}
