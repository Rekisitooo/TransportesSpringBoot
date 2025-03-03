package com.transports.spring.model.templategeneration.common;

import com.aspose.cells.*;
import com.transports.spring.exception.GeneratePdfFromExcelException;

import java.nio.file.Path;

public class TemplatePdf {

    protected TemplatePdf() {}

    public static void generate(final Path involvedExcelCalendarPath, final Path pdfPath) throws GeneratePdfFromExcelException {
        try {
            final String involvedCalendarPathString = involvedExcelCalendarPath.toString();
            final String pdfStringPath = pdfPath.toString();
            final Workbook excelCalendar = new Workbook(involvedCalendarPathString);
            excelCalendar.save(pdfStringPath);
        } catch (final Exception e) {
            throw new GeneratePdfFromExcelException(e);
        }
    }
}
