package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.dto.DtoTransportDateByTemplate;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
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
        final Map<LocalDate, DtoTransportDateByTemplate> dateMap = dtoPassengerBody.getMonthTransportDateByDayMap();
        final List<DtoTemplateDay> passengerAssistanceDates = dtoPassengerBody.getPassengerAssistanceDateList();

        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup = new DtoTemplateExcelTransportCellGroup(this.currentCol, this.currentRow, excelSheet, String.valueOf(currentDayOfMonth));

            final DtoTransportDateByTemplate dtoTransportDateByTemplate = dateMap.get(super.templateDate);
            if (isActualDateTransportDate(dtoTransportDateByTemplate)) {

                final DtoPassengerTransport passengerTransport = passengerTransportDateMap.get(super.templateDate);
                if (isPassengerArrangedInTransportInActualDate(passengerTransport)) {
                    dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + passengerTransport.getEventName());
                    dtoTemplateExcelTransportCellGroup.setHeaderText("Te lleva:");
                    dtoTemplateExcelTransportCellGroup.setBodyText(passengerTransport.getDriverFullName());

                    generateCustomTemplateExcelTransportDayCellGroup(dtoTemplateExcelTransportCellGroup);

                } else {
                    final String dateEventName = getAssistanceDateEventName(passengerAssistanceDates);
                    if (isPassengerAssistingOnActualDate(dateEventName)){
                        dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + dateEventName);
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

    private static void generateCustomTemplateExcelTransportDayCellGroup(final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup) {
        final DriverCustomTemplateExcelTransportDayCellGroup transportDayCell = new DriverCustomTemplateExcelTransportDayCellGroup();
        transportDayCell.generate(dtoTemplateExcelTransportCellGroup);
    }

    private static void generateDefaultTemplateExcelDayGroup(final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup) {
        final DefaultTemplateExcelDayCellGroup dayCellGroup = new DefaultTemplateExcelDayCellGroup();
        dayCellGroup.generate(dtoTemplateExcelTransportCellGroup);
    }

    private String getAssistanceDateEventName(final List<DtoTemplateDay> passengerAssistanceDates) {
        String eventName = "";
        for (final DtoTemplateDay dtoPassengerAssistanceDate : passengerAssistanceDates) {
            final LocalDate assistanceDateDate = dtoPassengerAssistanceDate.getDate();
            if (assistanceDateDate.isEqual(super.templateDate)) {
                eventName = dtoPassengerAssistanceDate.getEventName();
                break;
            }
        }

        return eventName;
    }

    private static boolean isPassengerArrangedInTransportInActualDate(final DtoPassengerTransport passengerTransport) {
        return passengerTransport != null;
    }

    private static boolean isPassengerAssistingOnActualDate(final String eventName) {
        return !"".equals(eventName);
    }

    private static boolean isActualDateTransportDate(final DtoTransportDateByTemplate templateTransportDate) {
        return templateTransportDate != null;
    }
}
