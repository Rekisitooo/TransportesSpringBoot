package com.transports.spring.model.templategeneration.common.cell.styler.passenger;

import com.transports.spring.model.templategeneration.common.cell.styler.AbstractDateCellGroupStyler;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class NoDriversAvailableForTransportDateCellStyler extends AbstractDateCellGroupStyler {

    public static final XSSFColor LIGHT_GRAY_BACKGROUND_COLOR = new XSSFColor(new byte[]{(byte) 217, (byte) 217, (byte) 217});
    public static final XSSFColor BlACK_FONT_COLOR = new XSSFColor(new byte[]{(byte) 0, (byte) 0, (byte) 0});

    public NoDriversAvailableForTransportDateCellStyler() {
        super();
    }

    @Override
    public XSSFCellStyle getNumberHeaderCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = super.getNumberHeaderCellStyle(excel);
        cellStyle.setFillForegroundColor(LIGHT_GRAY_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

    @Override
    public XSSFFont getNumberHeaderFont(final XSSFWorkbook excel) {
        final XSSFFont cellFont = super.getNumberHeaderFont(excel);
        cellFont.setColor(BlACK_FONT_COLOR);
        cellFont.setBold(true);

        return cellFont;
    }

    @Override
    public XSSFCellStyle getHeaderCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = super.getHeaderCellStyle(excel);
        cellStyle.setFillForegroundColor(LIGHT_GRAY_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

    @Override
    public XSSFCellStyle getBodyCellStyle(final XSSFWorkbook excel) {
        final XSSFCellStyle cellStyle = super.getBodyCellStyle(excel);
        cellStyle.setFillForegroundColor(LIGHT_GRAY_BACKGROUND_COLOR);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);

        return cellStyle;
    }
}
