package com.transports.spring.model.templategeneration.common;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import com.aspose.pdf.Document;
import com.aspose.pdf.devices.*;
import com.transports.spring.exception.GenerateJpgFromExcelException;

public class TemplateJpg {

    protected TemplateJpg() {}

    public static void generate(final Path involvedPdfCalendarPath, final Path involvedJpgCalendarPath, final String fileName) throws GenerateJpgFromExcelException {
        try {
            final String pdfPath = involvedPdfCalendarPath.toString();
            final Document document = new Document(pdfPath);
            final JpegDevice jpegDevice = new JpegDevice(new Resolution(300));
            final OutputStream imageStream = new FileOutputStream(involvedJpgCalendarPath + "/" + fileName + ".jpg");
            jpegDevice.process(document.getPages().get_Item(1), imageStream);
            imageStream.close();
            document.close();
        } catch (Exception e) {
            throw new GenerateJpgFromExcelException(e);
        }
    }
}
