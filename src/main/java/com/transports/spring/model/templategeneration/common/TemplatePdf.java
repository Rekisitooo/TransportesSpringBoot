package com.transports.spring.model.templategeneration.common;

import com.aspose.cells.*;
import com.transports.spring.exception.GeneratePdfFromExcelException;

import java.nio.file.Path;

public class TemplatePdf {

    protected TemplatePdf() {}

    public static Path generate(final Path involvedExcelCalendarPath, final Path pdfPath, final String fileName) throws GeneratePdfFromExcelException {
        try {
            final String involvedCalendarPathString = involvedExcelCalendarPath.toString();
            final String pdfStringPath = pdfPath + "/" + fileName + ".pdf";
            final Workbook excelCalendar = new Workbook(involvedCalendarPathString);
            excelCalendar.save(pdfStringPath);

            return Path.of(pdfStringPath);
        } catch (final Exception e) {
            throw new GeneratePdfFromExcelException(e);
        }
    }
}
