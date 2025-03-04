package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.model.templategeneration.common.cell.AbstractCustomTemplateExcelTransportDayCellGroup;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupBodyCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupHeaderCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupNumberCell;

public class CustomTemplateExcelTransportDayCellGroup extends AbstractCustomTemplateExcelTransportDayCellGroup {

    public CustomTemplateExcelTransportDayCellGroup(DayCellGroupNumberCell dayCellGroupNumberCell, DayCellGroupHeaderCell dayCellGroupHeaderCell, DayCellGroupBodyCell dayCellGroupBodyCell) {
        super(dayCellGroupNumberCell, dayCellGroupHeaderCell, dayCellGroupBodyCell);
    }
}
