package com.transports.spring.model.templategeneration.common.cell;

import com.transports.spring.model.templategeneration.common.cell.styler.NumberDayCellStyler;
import org.apache.poi.xssf.usermodel.*;

public class DayCellGroupNumberCell extends AbstractDayCellGroupCell {

    public DayCellGroupNumberCell() {
        super();
    }

    public void generate(final XSSFSheet excelSheet, final int currentRow, final int currentCol, final String currentDayOfMonth) {
        final XSSFCellStyle defaultCellStyle = NumberDayCellStyler.getCustomCellStyle(excelSheet.getWorkbook());
        super.generate(excelSheet, currentRow, currentCol, currentDayOfMonth, defaultCellStyle);
    }

    public void generateDefault(final XSSFSheet excelSheet, final int currentRow, final int currentCol, final String currentDayOfMonth) {
        final XSSFCellStyle defaultCellStyle = NumberDayCellStyler.getDefaultCellStyle(excelSheet.getWorkbook());
        super.generate(excelSheet, currentRow, currentCol, currentDayOfMonth, defaultCellStyle);
    }
}
