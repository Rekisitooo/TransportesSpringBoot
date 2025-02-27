package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.model.templategeneration.common.cell.AbstractCustomTemplateExcelTransportDayCellGroup;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupBodyCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupHeaderCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupNumberCell;

public class PassengerCustomTemplateExcelTransportDayCellGroup extends AbstractCustomTemplateExcelTransportDayCellGroup {

    public PassengerCustomTemplateExcelTransportDayCellGroup(DayCellGroupNumberCell dayCellGroupNumberCell, DayCellGroupHeaderCell dayCellGroupHeaderCell, DayCellGroupBodyCell dayCellGroupBodyCell) {
        super(dayCellGroupNumberCell, dayCellGroupHeaderCell, dayCellGroupBodyCell);
    }
}
