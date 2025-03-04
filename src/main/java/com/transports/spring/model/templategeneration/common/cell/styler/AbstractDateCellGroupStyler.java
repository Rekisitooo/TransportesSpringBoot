package com.transports.spring.model.templategeneration.common.cell.styler;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractDateCellBodyStyler {

    protected AbstractDateCellBodyStyler(){}

    protected static final String APTOS_NARROW_FONT = "Aptos Narrow";

    protected static XSSFCellStyle getNumberHeaderCellStyle(final XSSFWorkbook excel) {
        return excel.createCellStyle();
    }

    protected static XSSFFont getNumberHeaderFont(final XSSFWorkbook excel) {
        return excel.createFont();
    }

    protected static XSSFCellStyle getHeaderCellStyle(final XSSFWorkbook excel) {
        return excel.createCellStyle();
    }

    protected static XSSFFont getHeaderFont(final XSSFWorkbook excel) {
        return excel.createFont();
    }

    protected static XSSFCellStyle getBodyCellStyle(final XSSFWorkbook excel) {
        return excel.createCellStyle();
    }

    protected static XSSFFont getBodyFont(final XSSFWorkbook excel) {
        return excel.createFont();
    }
}
