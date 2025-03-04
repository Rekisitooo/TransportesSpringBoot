package com.transports.spring.model.templategeneration.common.cell.styler;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

public class TransportDateCellStyler extends AbstractDateCellGroupStyler {

    public static final XSSFColor LIGHT_BLUE_BACKGROUND_COLOR = new XSSFColor(new byte[]{(byte) 218, (byte) 233, (byte) 248});
    public static final XSSFColor BLUE_FONT_COLOR = new XSSFColor(new byte[]{(byte) 0, (byte) 112, (byte) 192});

    public TransportDateCellStyler() {
        super();
    }

    @Override
    public XSSFCellStyle getNumberHeaderCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getNumberHeaderFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setFillForegroundColor(LIGHT_BLUE_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setIndention((short) 1);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    @Override
    public XSSFFont getNumberHeaderFont(final XSSFWorkbook excel) {
        final XSSFFont cellFont = excel.createFont();
        cellFont.setColor(BLUE_FONT_COLOR);
        cellFont.setBold(true);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }

    @Override
    public XSSFCellStyle getHeaderCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getHeaderFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setIndention((short) 1);
        cellStyle.setFillForegroundColor(LIGHT_BLUE_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

    @Override
    public XSSFFont getHeaderFont(final XSSFWorkbook excel) {
        final XSSFFont cellFont = excel.createFont();
        cellFont.setBold(true);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }

    @Override
    public XSSFCellStyle getBodyCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getBodyFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setWrapText(true);
        cellStyle.setFillForegroundColor(LIGHT_BLUE_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setIndention((short) 1);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);

        return cellStyle;
    }

    @Override
    public XSSFFont getBodyFont(final XSSFWorkbook excel) {
        final XSSFFont cellFont = excel.createFont();
        cellFont.setBold(false);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }
}
