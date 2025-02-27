package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import org.apache.poi.xssf.usermodel.*;

public abstract class AbstractCustomTemplateExcelTransportDayCellGroup {

    protected final DayCellGroupNumberCell dayCellGroupNumberCell;
    protected final DayCellGroupHeaderCell dayCellGroupHeaderCell;
    protected final DayCellGroupBodyCell dayCellGroupBodyCell;

    protected AbstractCustomTemplateExcelTransportDayCellGroup(DayCellGroupNumberCell dayCellGroupNumberCell, DayCellGroupHeaderCell dayCellGroupHeaderCell, DayCellGroupBodyCell dayCellGroupBodyCell) {
        this.dayCellGroupNumberCell = dayCellGroupNumberCell;
        this.dayCellGroupHeaderCell = dayCellGroupHeaderCell;
        this.dayCellGroupBodyCell = dayCellGroupBodyCell;
    }

    public void generate(final DtoTemplateExcelTransportCellGroup cellGroupDto) {
        final XSSFSheet excelSheet = cellGroupDto.getExcelSheet();
        final Integer row = cellGroupDto.getExcelRow();
        final Integer column = cellGroupDto.getExcelCol();

        this.dayCellGroupNumberCell.generate(excelSheet, row, column, cellGroupDto.getCellNumberText());
        this.dayCellGroupHeaderCell.generate(excelSheet, row + 1, column, cellGroupDto.getCellNumberText());
        this.dayCellGroupBodyCell.generate(excelSheet, row + 2, column, cellGroupDto.getBodyText());
    }
}
