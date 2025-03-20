package com.transports.spring.model.templategeneration.common.cell.styler;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EventDateCellStyler extends AbstractDateCellGroupStyler {

    public static final XSSFColor PURPLE_BACKGROUND_COLOR = new XSSFColor(new byte[]{(byte) 242, (byte) 206, (byte) 239});
    public static final XSSFColor PURPLE_FONT_COLOR = new XSSFColor(new byte[]{(byte) 120, (byte) 33, (byte) 112});

    public EventDateCellStyler() {
        super();
    }

    @Override
    public XSSFCellStyle getNumberHeaderCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getNumberHeaderFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setFillForegroundColor(PURPLE_BACKGROUND_COLOR);
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
        cellFont.setColor(PURPLE_FONT_COLOR);
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
        cellStyle.setFillForegroundColor(PURPLE_BACKGROUND_COLOR);
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
        cellStyle.setFillForegroundColor(PURPLE_BACKGROUND_COLOR);
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
        cellFont.setBold(true);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }
}
