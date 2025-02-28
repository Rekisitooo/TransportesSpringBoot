package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.templategeneration.common.AbstractTemplateExcelBodyGenerator;
import com.transports.spring.model.templategeneration.common.cell.DefaultTemplateExcelDayCellGroup;
import org.apache.poi.xssf.usermodel.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverTemplateExcelBodyGenerator extends AbstractTemplateExcelBodyGenerator {

    public DriverTemplateExcelBodyGenerator(final Calendar templateMonthCalendar) {
        super(templateMonthCalendar);
    }

    public void generate(final XSSFSheet excelSheet, final DtoTemplateExcelDriverBody dtoDriverBody) {
        final Map<Integer, DtoDriverTransport> transportForDayMap = getTransportForDayMap(dtoDriverBody);

        for (int currentDayOfMonth = 1; currentDayOfMonth <= this.lastMonthDay; currentDayOfMonth++) {
            jumpToNextRowIfOutOfScope();
            final DtoTemplateExcelTransportCellGroup dtoTemplateExcelTransportCellGroup = new DtoTemplateExcelTransportCellGroup(this.currentCol, this.currentRow, excelSheet, String.valueOf(currentDayOfMonth));

            final DtoDriverTransport driverTransport = transportForDayMap.get(currentDayOfMonth);
            if (driverTransport == null) {
                final DefaultTemplateExcelDayCellGroup dayCellGroup = new DefaultTemplateExcelDayCellGroup();
                dayCellGroup.generate(dtoTemplateExcelTransportCellGroup);
            } else {
                final String eventName = driverTransport.getEventName();
                dtoTemplateExcelTransportCellGroup.setCellNumberText(currentDayOfMonth + " " + eventName);
                dtoTemplateExcelTransportCellGroup.setHeaderText("Llevas a:");
                dtoTemplateExcelTransportCellGroup.setBodyText(driverTransport.getPassengerFullNames());

                final DriverCustomTemplateExcelTransportDayCellGroup transportDayCell = new DriverCustomTemplateExcelTransportDayCellGroup();
                transportDayCell.generate(dtoTemplateExcelTransportCellGroup);
            }

            this.currentCol += 2;
        }
    }

    private static Map<Integer, DtoDriverTransport> getTransportForDayMap(DtoTemplateExcelDriverBody templateExcelDriverBody) {
        final Map<Integer, DtoDriverTransport> transportForDayMap = new HashMap<>();

        final List<DtoDriverTransport> allTemplateDriverTransports = templateExcelDriverBody.getAllTemplateDriverTransports();
        for (final DtoDriverTransport dtoDriverTransport : allTemplateDriverTransports) {
            final String transportDateString = dtoDriverTransport.getTransportDate();
            final LocalDate transportDate = LocalDate.parse(transportDateString);

            transportForDayMap.put(transportDate.getDayOfMonth(), dtoDriverTransport);
        }
        return transportForDayMap;
    }
}
