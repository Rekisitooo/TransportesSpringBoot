package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.model.templategeneration.common.cell.AbstractCustomTemplateExcelTransportDayCellGroup;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupBodyCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupHeaderCell;
import com.transports.spring.model.templategeneration.common.cell.DayCellGroupNumberCell;

public class DriverCustomTemplateExcelTransportDayCellGroup extends AbstractCustomTemplateExcelTransportDayCellGroup {

    public DriverCustomTemplateExcelTransportDayCellGroup() {
        super(new DayCellGroupNumberCell(), new DayCellGroupHeaderCell(), new DayCellGroupBodyCell());
    }
}
