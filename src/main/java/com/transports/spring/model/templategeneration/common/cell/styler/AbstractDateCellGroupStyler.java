package com.transports.spring.model.templategeneration.common.cell.styler;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractDateCellGroupStyler {

    protected AbstractDateCellGroupStyler(){}

    protected static final String APTOS_NARROW_FONT = "Aptos Narrow";

    public XSSFCellStyle getNumberHeaderCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getNumberHeaderFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setIndention((short) 1);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    public XSSFFont getNumberHeaderFont(final XSSFWorkbook excel) {
        return getDefaultFont(excel);
    }

    public XSSFCellStyle getHeaderCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getHeaderFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setIndention((short) 1);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    public XSSFFont getHeaderFont(final XSSFWorkbook excel) {
        return getDefaultFont(excel);
    }

    public XSSFCellStyle getBodyCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = excel.createCellStyle();
        final XSSFFont cellFont = getBodyFont(excel);
        cellStyle.setFont(cellFont);
        cellStyle.setIndention((short) 1);
        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    public XSSFFont getBodyFont(final XSSFWorkbook excel) {
        return getDefaultFont(excel);
    }

    private static XSSFFont getDefaultFont(final XSSFWorkbook excel) {
        final XSSFFont cellFont = excel.createFont();
        cellFont.setBold(false);
        cellFont.setFontName(APTOS_NARROW_FONT);

        return cellFont;
    }
}
