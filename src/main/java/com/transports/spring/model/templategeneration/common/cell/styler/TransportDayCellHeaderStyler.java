package com.transports.spring.model.templategeneration.common.cell.styler;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static com.transports.spring.model.templategeneration.common.cell.styler.TransportDayCellStyler.*;

public class TransportDayCellHeaderStyler {

    private TransportDayCellHeaderStyler() {}

    public static XSSFFont getFont(final XSSFWorkbook excel) {
        final XSSFFont cellFont = excel.createFont();
        cellFont.setBold(true);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }

    public static XSSFCellStyle getCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setIndention((short) 1);
        cellStyle.setFillForegroundColor(TransportDayCellStyler.LIGHT_BLUE_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }
}
