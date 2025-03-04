package com.transports.spring.model.templategeneration.common.cell.styler;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;

public class TransportDayCellStyler {

    public static final XSSFColor LIGHT_BLUE_BACKGROUND_COLOR = new XSSFColor(new byte[]{(byte) 218, (byte) 233, (byte) 248});
    public static final XSSFColor BLUE_FONT_COLOR = new XSSFColor(new byte[]{(byte) 0, (byte) 112, (byte) 192});
    public static final String APTOS_NARROW_FONT = "Aptos Narrow";

    private TransportDayCellStyler() {}

    public static XSSFCellStyle getCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setFillForegroundColor(TransportDayCellStyler.LIGHT_BLUE_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setIndention((short) 1);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    public static XSSFFont getFont(final XSSFWorkbook excel) {
        final XSSFFont cellFont = excel.createFont();
        cellFont.setColor(TransportDayCellStyler.BLUE_FONT_COLOR);
        cellFont.setBold(true);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }
}
