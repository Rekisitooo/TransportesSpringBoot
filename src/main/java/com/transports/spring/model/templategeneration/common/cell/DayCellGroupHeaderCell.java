package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.model.templategeneration.common.cell.styler.TransportDayCellHeaderStyler;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DayCellGroupHeaderCell extends AbstractDayCellGroupCell {

    public DayCellGroupHeaderCell() {
        super();
    }

    public void generate(final XSSFSheet excelSheet, final int currentRow, final int currentCol, final String currentDayOfMonth) {
        final XSSFCellStyle cellStyle = TransportDayCellHeaderStyler.getCellStyle(excelSheet.getWorkbook());
        super.generate(excelSheet, currentRow, currentCol, currentDayOfMonth, cellStyle);
    }

}
