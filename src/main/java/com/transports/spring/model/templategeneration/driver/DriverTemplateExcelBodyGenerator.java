package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.templategeneration.common.AbstractTemplateExcelBodyGenerator;
import com.transports.spring.model.templategeneration.common.cell.styler.DefaultDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.TransportDateCellStyler;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;

public class DriverTemplateExcelBodyGenerator extends AbstractTemplateExcelBodyGenerator {

    public DriverTemplateExcelBodyGenerator(final Calendar templateMonthCalendar, final LocalDate templateDate) {
        super(templateMonthCalendar, templateDate);
    }

    public void generate(final XSSFSheet excelSheet, final DtoTemplateExcelDriverBody dtoDriverBody) {
        final Map<LocalDate, DtoDriverTransport> driverTransportForDayMap = dtoDriverBody.getDriverTransportForDayMap();

        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup = new DtoTemplateExcelTransportCellGroup(this.currentCol, this.currentRow, excelSheet, String.valueOf(currentDayOfMonth));

            final DtoDriverTransport driverTransport = driverTransportForDayMap.get(super.templateDate);
            if (isDriverInvolvedInTransportInActualDate(driverTransport)) {
                final String eventName = driverTransport.getEventName();
                dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + eventName);
                dtoTemplateExcelTransportCellGroup.setHeaderText("Llevas a:");
                dtoTemplateExcelTransportCellGroup.setBodyText(driverTransport.getPassengerFullNames());
                dtoTemplateExcelTransportCellGroup.setCellStyler(new TransportDateCellStyler());
            } else {
                dtoTemplateExcelTransportCellGroup.setCellStyler(new DefaultDateCellStyler());
            }

            super.generateCustomTemplateExcelTransportDayCellGroup(dtoTemplateExcelTransportCellGroup, excelSheet);

            this.currentCol += 2;
            super.templateDate = super.templateDate.plusDays(1);
        }
    }

    private static boolean isDriverInvolvedInTransportInActualDate(DtoDriverTransport driverTransport) {
        return driverTransport != null;
    }
}
