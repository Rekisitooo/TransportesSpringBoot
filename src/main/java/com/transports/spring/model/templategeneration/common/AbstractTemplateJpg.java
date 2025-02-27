package com.transports.spring.model.templategeneration.common;

import com.aspose.cells.ImageOrPrintOptions;
import com.aspose.cells.ImageType;
import com.aspose.cells.SheetRender;
import com.aspose.cells.Worksheet;

import java.nio.file.Path;

public abstract class AbstractTemplateJpg {

    protected AbstractTemplateJpg() {}

    public void generate(final Path involvedCalendarPath) {
        try {
            final String involvedCalendarPathString = involvedCalendarPath.toString();
            final com.aspose.cells.Workbook calendar = new com.aspose.cells.Workbook(involvedCalendarPathString);

            final ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();
            imgOptions.setImageType(ImageType.JPEG);

            final Worksheet sheetToConvert = calendar.getWorksheets().get(0);
            final SheetRender sr = new SheetRender(sheetToConvert, imgOptions);

            final int dotExtension = involvedCalendarPathString.lastIndexOf(".");
            final String involvedCalendarPathNoExtension = involvedCalendarPathString.substring(0, dotExtension);
            final String jpgPassengerCalendarPath = involvedCalendarPathNoExtension + ".jpg";

            sr.toImage(0, jpgPassengerCalendarPath);

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
