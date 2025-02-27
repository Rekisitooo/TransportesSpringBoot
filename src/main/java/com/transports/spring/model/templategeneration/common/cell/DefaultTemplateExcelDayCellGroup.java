package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;

public class DefaultTemplateExcelDayCellGroup {

    protected DayCellGroupNumberCell dayCellGroupNumberCell;

    public DefaultTemplateExcelDayCellGroup() {
        this.dayCellGroupNumberCell = new DayCellGroupNumberCell();
    }

    public void generate(final DtoTemplateExcelTransportCellGroup cellGroupDto) {
        this.dayCellGroupNumberCell.generateDefault(
                cellGroupDto.getExcelSheet(),
                cellGroupDto.getExcelRow(),
                cellGroupDto.getExcelCol(),
                cellGroupDto.getCellNumberText()
        );
    }
}
