package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.model.templategeneration.common.cell.AbstractTemplateExcelTransportDayCellGroup;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupBodyCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupHeaderCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupNumberCell;

public class TemplateExcelTransportDayCellGroup extends AbstractTemplateExcelTransportDayCellGroup {

    public TemplateExcelTransportDayCellGroup(DayCellGroupNumberCell dayCellGroupNumberCell, DayCellGroupHeaderCell dayCellGroupHeaderCell, DayCellGroupBodyCell dayCellGroupBodyCell) {
        super(dayCellGroupNumberCell, dayCellGroupHeaderCell, dayCellGroupBodyCell);
    }
}
