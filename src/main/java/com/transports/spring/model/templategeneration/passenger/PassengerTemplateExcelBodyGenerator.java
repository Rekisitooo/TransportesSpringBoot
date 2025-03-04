package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.dto.DtoTransportDateByTemplate;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.Event;
import com.transports.spring.model.templategeneration.common.AbstractTemplateExcelBodyGenerator;

import com.transports.spring.model.templategeneration.common.cell.styler.DefaultDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.EventDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.TransportDateCellStyler;
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
        final Map<LocalDate, Event> dateEventMap = dtoPassengerBody.getDateEventMap();

        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup =
                    new DtoTemplateExcelTransportCellGroup(this.currentCol, this.currentRow, excelSheet, String.valueOf(currentDayOfMonth));

            final DtoTransportDateByTemplate dtoTransportDateByTemplate = dateMap.get(super.templateDate);
            if (isActualDateTransportDate(dtoTransportDateByTemplate)) {

                final DtoPassengerTransport passengerTransport = passengerTransportDateMap.get(super.templateDate);
                if (isPassengerArrangedInTransportInActualDate(passengerTransport)) {
                    dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + passengerTransport.getEventName());
                    dtoTemplateExcelTransportCellGroup.setHeaderText("Te lleva:");
                    dtoTemplateExcelTransportCellGroup.setBodyText(passengerTransport.getDriverFullName());
                    dtoTemplateExcelTransportCellGroup.setCellStyler(new TransportDateCellStyler());
                } else {
                    final String dateEventName = getAssistanceDateEventName(passengerAssistanceDates);
                    if (isPassengerAssistingOnActualDate(dateEventName)) {
                        dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + dateEventName);
                        dtoTemplateExcelTransportCellGroup.setBodyText("No hay conductores disponibles.");
                        dtoTemplateExcelTransportCellGroup.setCellStyler(new TransportDateCellStyler());
                    } else {
                        dtoTemplateExcelTransportCellGroup.setCellStyler(new DefaultDateCellStyler());
                    }
                }
            } else {
                final Event event = dateEventMap.get(super.templateDate);
                if (event == null) {
                    dtoTemplateExcelTransportCellGroup.setCellStyler(new DefaultDateCellStyler());
                } else {
                    dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + event.getEventName());
                    dtoTemplateExcelTransportCellGroup.setBodyText("No hay arreglos.");
                    dtoTemplateExcelTransportCellGroup.setCellStyler(new EventDateCellStyler());
                }
            }

            generateCustomTemplateExcelTransportDayCellGroup(dtoTemplateExcelTransportCellGroup, excelSheet);

            this.currentCol += 2;
            super.templateDate = super.templateDate.plusDays(1);
        }
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
