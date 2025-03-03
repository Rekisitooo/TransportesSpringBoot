package com.transports.spring.model.templategeneration.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import com.aspose.pdf.Document;
import com.aspose.pdf.devices.*;
import com.transports.spring.exception.GenerateJpgFromExcelException;

public class TemplateJpg {

    protected TemplateJpg() {}

    public static void generate(final Path involvedPdfCalendarPath, final Path involvedJpgCalendarPath) throws GenerateJpgFromExcelException {
        final String pdfPath = involvedPdfCalendarPath.toString();
        try (final Document document = new Document(pdfPath)) {
            final JpegDevice jpegDevice = new JpegDevice(new Resolution(300));
            final OutputStream imageStream = createImageStream(involvedJpgCalendarPath);
            jpegDevice.process(document.getPages().get_Item(1), imageStream);
        }
    }

    private static OutputStream createImageStream(final Path involvedJpgCalendarPath) throws GenerateJpgFromExcelException {
        try {
            return new FileOutputStream(involvedJpgCalendarPath.toFile());
        } catch (final FileNotFoundException e) {
            throw new GenerateJpgFromExcelException(e);
        }
    }
}
