package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelTransportCellGroup;
import com.transports.spring.model.templategeneration.common.cell.styler.AbstractDateCellGroupStyler;
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
        final AbstractDateCellGroupStyler cellStyler = cellGroupDto.getCellStyler();

        this.dayCellGroupNumberCell.generate(excelSheet, row, column, cellGroupDto.getCellNumberText(), cellStyler);
        this.dayCellGroupHeaderCell.generate(excelSheet, row + 1, column, cellGroupDto.getHeaderText(), cellStyler);
        this.dayCellGroupBodyCell.generate(excelSheet, row + 2, column, cellGroupDto.getBodyText(), cellStyler);
    }
}
