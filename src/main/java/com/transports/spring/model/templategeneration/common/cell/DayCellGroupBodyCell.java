package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.model.templategeneration.common.cell.styler.TransportDayCellBodyStyler;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DayCellGroupBodyCell extends AbstractDayCellGroupCell {

    public DayCellGroupBodyCell() {
        super();
    }

    public void generate(final XSSFSheet excelSheet, final int currentRow, final int currentCol, final String text) {
        final XSSFCellStyle defaultCellStyle = TransportDayCellBodyStyler.getCellStyle(excelSheet.getWorkbook());
        super.generate(excelSheet, currentRow, currentCol, text, defaultCellStyle);
    }

}
