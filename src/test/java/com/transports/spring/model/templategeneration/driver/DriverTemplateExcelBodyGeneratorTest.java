package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.templategeneration.common.cell.styler.AbstractDateCellGroupStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.DefaultDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.EventDateCellStyler;
import com.transports.spring.model.templategeneration.common.cell.styler.TransportDateCellStyler;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class DriverTemplateExcelBodyGeneratorTest {

    @Test
    void testEmptyDate() {
        final DriverTemplateExcelBodyGenerator bodyGenerator = new DriverTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null,1, null,null);

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
    void testCellGroupForTransportDatesButDriverNotInvolvedInTransport() {
        final DriverTemplateExcelBodyGenerator bodyGenerator = new DriverTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,
                new DtoTemplateDate("Reunión Vida y Ministerio", "transportDate"),
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
    void testCellGroupForTransportDatesWithDriverInvolvedInTransport() {
        final DriverTemplateExcelBodyGenerator bodyGenerator = new DriverTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,
                new DtoTemplateDate("aaaaaaaaaaa", "transportDate"),
                new DtoDriverTransport("passengers, passengers", "Reunión Vida y Ministerio")
        );

        final AbstractDateCellGroupStyler cellStyler = dtoCellGroup.getCellStyler();
        final boolean isCorrectCellStyle = (cellStyler instanceof TransportDateCellStyler);
        final boolean isNumberCellTextOk = dtoCellGroup.getCellNumberText().contains("1") && dtoCellGroup.getCellNumberText().contains("Reunión Vida y Ministerio");
        final boolean isHeaderTextOK = dtoCellGroup.getHeaderText().equalsIgnoreCase("Llevas a:");
        final boolean isBodyTextOK = dtoCellGroup.getBodyText().equalsIgnoreCase("passengers, passengers");

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
        final DriverTemplateExcelBodyGenerator bodyGenerator = new DriverTemplateExcelBodyGenerator(Calendar.getInstance(), LocalDate.now());
        final DtoTemplateExcelTransportCellGroup dtoCellGroup = bodyGenerator.getDtoTemplateExcelTransportCellGroup(
                null, 1,
                new DtoTemplateDate("Asamblea", "event"),
                new DtoDriverTransport("passengers, passengers")
        );

        final AbstractDateCellGroupStyler cellStyler = dtoCellGroup.getCellStyler();
        final boolean isCorrectCellStyle = (cellStyler instanceof EventDateCellStyler);
        final boolean isNumberCellTextOk = dtoCellGroup.getCellNumberText().contains("1") && dtoCellGroup.getCellNumberText().contains("Asamblea");
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