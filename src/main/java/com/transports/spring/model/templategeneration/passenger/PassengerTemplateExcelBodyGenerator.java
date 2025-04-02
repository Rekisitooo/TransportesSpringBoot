package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.templategeneration.common.AbstractTemplateExcelBodyGenerator;
import com.transports.spring.model.templategeneration.common.cell.styler.DefaultDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.EventDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.TransportDateCellStyler;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;

public class PassengerTemplateExcelBodyGenerator extends AbstractTemplateExcelBodyGenerator {

    public PassengerTemplateExcelBodyGenerator(final Calendar templateMonthCalendar, final LocalDate templateDate) {
        super(templateMonthCalendar, templateDate);
    }

    public void generate(final XSSFSheet excelSheet, final DtoTemplateExcelPassengerBody dtoPassengerBody) {
        final Map<LocalDate, DtoPassengerTransport> passengerTransportDateMap = dtoPassengerBody.getAllTemplatePassengerTransportsByDayMap();
        final Map<LocalDate, DtoTemplateDate> transportDateMap = dtoPassengerBody.getMonthTransportDateByDayMap();
        final Map<LocalDate, DtoTemplateDay> passengerAssistanceDates = dtoPassengerBody.getPassengerAssistanceDateList();

        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup =
                    new DtoTemplateExcelTransportCellGroup(this.currentCol, this.currentRow, excelSheet, String.valueOf(currentDayOfMonth));

            final DtoTemplateDate dtoTemplateTransportDate = transportDateMap.get(super.templateDate);
            final DtoTemplateDay dtoPassengerAssistance = passengerAssistanceDates.get(super.templateDate);

            if (dtoTemplateTransportDate != null && dtoPassengerAssistance != null) {
                if (dtoTemplateTransportDate.getDateType().equals("event") || dtoPassengerAssistance.getNeedsTransport() == 0) {
                    dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + dtoTemplateTransportDate.getEventName());
                    dtoTemplateExcelTransportCellGroup.setBodyText("No hay arreglos.");
                    dtoTemplateExcelTransportCellGroup.setCellStyler(new EventDateCellStyler());

                } else if (dtoTemplateTransportDate.getDateType().equals("transportDate")) {
                    final DtoPassengerTransport passengerTransport = passengerTransportDateMap.get(super.templateDate);

                    if (isPassengerArrangedInTransportInActualDate(passengerTransport)) {
                        dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + passengerTransport.getEventName());
                        dtoTemplateExcelTransportCellGroup.setHeaderText("Te lleva:");
                        dtoTemplateExcelTransportCellGroup.setBodyText(passengerTransport.getDriverFullName());
                        dtoTemplateExcelTransportCellGroup.setCellStyler(new TransportDateCellStyler());
                    } else {
                        final String dateEventName = dtoPassengerAssistance.getEventName();
                        if (isPassengerAssistingOnActualDate(dateEventName)) {
                            dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + dateEventName);
                            dtoTemplateExcelTransportCellGroup.setBodyText("No hay conductores disponibles.");
                            dtoTemplateExcelTransportCellGroup.setCellStyler(new TransportDateCellStyler());
                        } else {
                            dtoTemplateExcelTransportCellGroup.setCellStyler(new DefaultDateCellStyler());
                        }
                    }
                } else {
                    dtoTemplateExcelTransportCellGroup.setCellStyler(new DefaultDateCellStyler());
                }
            } else {
                dtoTemplateExcelTransportCellGroup.setCellStyler(new DefaultDateCellStyler());
            }

            super.generateCustomTemplateExcelTransportDayCellGroup(dtoTemplateExcelTransportCellGroup, excelSheet);

            this.currentCol += 2;
            super.templateDate = super.templateDate.plusDays(1);
        }
    }

    private static boolean isPassengerArrangedInTransportInActualDate(final DtoPassengerTransport passengerTransport) {
        return passengerTransport != null;
    }

    private static boolean isPassengerAssistingOnActualDate(final String eventName) {
        return !"".equals(eventName);
    }

}
