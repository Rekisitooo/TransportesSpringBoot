package com.transports.spring.model.templategeneration.common.cell.styler;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static com.transports.spring.model.templategeneration.common.cell.styler.TransportDayCellStyler.APTOS_NARROW_FONT;

public class NumberDayCellStyler {

    private NumberDayCellStyler() {}

    public static XSSFCellStyle getCustomCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getCustomFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setFillForegroundColor(TransportDayCellStyler.LIGHT_BLUE_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setIndention((short) 1);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    private static XSSFFont getCustomFont(final XSSFWorkbook excel) {
        final XSSFFont cellFont = excel.createFont();
        cellFont.setColor(TransportDayCellStyler.BLUE_FONT_COLOR);
        cellFont.setBold(true);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }

    public static XSSFCellStyle getDefaultCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getDefaultFontStyle(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setIndention((short) 1);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    private static XSSFFont getDefaultFontStyle(final XSSFWorkbook excel) {
        final XSSFFont cellFont = excel.createFont();
        cellFont.setBold(false);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }
}
