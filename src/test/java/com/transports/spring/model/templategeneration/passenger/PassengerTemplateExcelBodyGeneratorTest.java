package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.templategeneration.common.cell.styler.AbstractDateCellGroupStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.DefaultDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.EventDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.TransportDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.passenger.NoDriversAvailableForTransportDateCellStyler;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PassengerTemplateExcelBodyGeneratorTest {

    @Test
    void testEmptyDate() {
        final PassengerTemplateExcelBodyGenerator bodyGenerator = new PassengerTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,null, null,null);

        final AbstractDateCellGroupStyler cellStyler = dtoCellGroup.getCellStyler();
        final boolean isCorrectCellStyle = (cellStyler instanceof DefaultDateCellStyler);
        final boolean isNumberCellTextOk = dtoCellGroup.getCellNumberText().equalsIgnoreCase("1");
        final boolean isHeaderTextOK = dtoCellGroup.getHeaderText() == null || dtoCellGroup.getHeaderText().equalsIgnoreCase("");
        final boolean isBodyTextOK = dtoCellGroup.getBodyText() == null || dtoCellGroup.getBodyText().equalsIgnoreCase("");

        if (isCorrectCellStyle &&
                isNumberCellTextOk &&
                isHeaderTextOK &&
                isBodyTextOK
        ) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    @Test
    void testTransportDateButCurrentPassengerDoesNotAssist() {
        final PassengerTemplateExcelBodyGenerator bodyGenerator = new PassengerTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,
                new DtoTemplateDate("transportDate", "Reunión Vida y Ministerio"),
                new DtoTemplateDay(""),
                null
        );

        final AbstractDateCellGroupStyler cellStyler = dtoCellGroup.getCellStyler();
        final boolean isCorrectCellStyle = (cellStyler instanceof DefaultDateCellStyler);
        final boolean isNumberCellTextOk = dtoCellGroup.getCellNumberText().equalsIgnoreCase("1");
        final boolean isHeaderTextOK = dtoCellGroup.getHeaderText() == null || dtoCellGroup.getHeaderText().equalsIgnoreCase("");
        final boolean isBodyTextOK = dtoCellGroup.getBodyText() == null || dtoCellGroup.getBodyText().equalsIgnoreCase("");

        if (isCorrectCellStyle &&
                isNumberCellTextOk &&
                isHeaderTextOK &&
                isBodyTextOK
        ) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    @Test
    void testCellGroupForTransportDatesButNoDriversForCurrentPassenger() {
        final PassengerTemplateExcelBodyGenerator bodyGenerator = new PassengerTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,
                new DtoTemplateDate("Reunión Vida y Ministerio", "transportDate"),
                new DtoTemplateDay("transport Date but no drivers for you"),
                null
        );

        final AbstractDateCellGroupStyler cellStyler = dtoCellGroup.getCellStyler();
        final boolean isCorrectCellStyle = (cellStyler instanceof NoDriversAvailableForTransportDateCellStyler);
        final boolean isNumberCellTextOk = dtoCellGroup.getCellNumberText().contains("1") && dtoCellGroup.getCellNumberText().contains("transport Date but no drivers for you");
        final boolean isHeaderTextOK = dtoCellGroup.getHeaderText() == null || dtoCellGroup.getHeaderText().equalsIgnoreCase("");
        final boolean isBodyTextOK = dtoCellGroup.getBodyText().equalsIgnoreCase("No hay suficientes conductores disponibles en el arreglo.");

        if (isCorrectCellStyle &&
                isNumberCellTextOk &&
                isHeaderTextOK &&
                isBodyTextOK
        ) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    @Test
    void testCellGroupForTransportDates() {
        final PassengerTemplateExcelBodyGenerator bodyGenerator = new PassengerTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,
                new DtoTemplateDate("Reunión Vida y Ministerio", "transportDate"),
                new DtoTemplateDay(),
                new DtoPassengerTransport("Día de transporte", "Pepito de los palotes")
        );

        final AbstractDateCellGroupStyler cellStyler = dtoCellGroup.getCellStyler();
        final boolean isCorrectCellStyle = (cellStyler instanceof TransportDateCellStyler);
        final boolean isNumberCellTextOk = dtoCellGroup.getCellNumberText().contains("1") && dtoCellGroup.getCellNumberText().contains("Día de transporte");
        final boolean isHeaderTextOK = dtoCellGroup.getHeaderText().equalsIgnoreCase("Te lleva:");
        final boolean isBodyTextOK = dtoCellGroup.getBodyText().equalsIgnoreCase("Pepito de los palotes");

        if (isCorrectCellStyle &&
                isNumberCellTextOk &&
                isHeaderTextOK &&
                isBodyTextOK
        ) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    @Test
    void testCellGroupForEvents() {
        final PassengerTemplateExcelBodyGenerator bodyGenerator = new PassengerTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,
                new DtoTemplateDate("Asamblea", "event"),
                new DtoTemplateDay(),
                null);

        testCellGroupForEventsAndNoNeedTransportDates(dtoCellGroup, "Asamblea");
    }

    @Test
    void testCellGroupForNoTransportNeedDays() {
        final PassengerTemplateExcelBodyGenerator bodyGenerator = new PassengerTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,
                new DtoTemplateDate("Reunión Vida y Ministerio", "transportDate"),
                new DtoTemplateDay(0),
                null);

        testCellGroupForEventsAndNoNeedTransportDates(dtoCellGroup, "Reunión Vida y Ministerio");

    }

    private static void testCellGroupForEventsAndNoNeedTransportDates(DtoTemplateExcelTransportCellGroup dtoCellGroup, String eventName) {
        final AbstractDateCellGroupStyler cellStyler = dtoCellGroup.getCellStyler();
        final boolean isCorrectCellStyle = (cellStyler instanceof EventDateCellStyler);
        final boolean isNumberCellTextOk = dtoCellGroup.getCellNumberText().contains("1") && dtoCellGroup.getCellNumberText().contains(eventName);
        final boolean isHeaderTextOK = dtoCellGroup.getHeaderText() == null || dtoCellGroup.getHeaderText().equalsIgnoreCase("");
        final boolean isBodyTextOK = dtoCellGroup.getBodyText().equalsIgnoreCase("No hay arreglos.");

        if (isCorrectCellStyle &&
                isNumberCellTextOk &&
                isHeaderTextOK &&
                isBodyTextOK
        ) {
            assertTrue(true);
        } else {
            fail();
        }
    }
}