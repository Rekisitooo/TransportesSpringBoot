package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.templategeneration.common.cell.styler.AbstractDateCellGroupStyler;

public abstract class AbstractTemplateExcelTransportDayCellGroup {

    protected final DayCellGroupNumberCell dayCellGroupNumberCell;
    protected final DayCellGroupHeaderCell dayCellGroupHeaderCell;
    protected final DayCellGroupBodyCell dayCellGroupBodyCell;

    protected AbstractTemplateExcelTransportDayCellGroup(DayCellGroupNumberCell dayCellGroupNumberCell, DayCellGroupHeaderCell dayCellGroupHeaderCell, DayCellGroupBodyCell dayCellGroupBodyCell) {
        this.dayCellGroupNumberCell = dayCellGroupNumberCell;
        this.dayCellGroupHeaderCell = dayCellGroupHeaderCell;
        this.dayCellGroupBodyCell = dayCellGroupBodyCell;
    }

    public void generate(final DtoTemplateExcelTransportCellGroup cellGroupDto) {
        final AbstractDateCellGroupStyler cellStyler = cellGroupDto.getCellStyler();

        this.dayCellGroupNumberCell.generate(cellGroupDto.getCellNumberText(), cellStyler);
        this.dayCellGroupHeaderCell.generate(cellGroupDto.getHeaderText(), cellStyler);
        this.dayCellGroupBodyCell.generate(cellGroupDto.getBodyText(), cellStyler);
    }
}
