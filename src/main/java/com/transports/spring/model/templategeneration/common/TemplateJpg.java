package com.transports.spring.model.templategeneration.common;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;

import com.aspose.pdf.Document;
import com.aspose.pdf.devices.*;
import com.transports.spring.exception.GenerateJpgFromExcelException;

import javax.imageio.ImageIO;

public class TemplateJpg {

    public static final int HEIGHT_TO_CROP = 150;

    protected TemplateJpg() {}

    public static void generate(final Path involvedPdfCalendarPath, final Path involvedJpgCalendarPath) throws GenerateJpgFromExcelException {
        final String pdfPath = involvedPdfCalendarPath.toString();
        try (final Document document = new Document(pdfPath)) {
            final JpegDevice jpegDevice = new JpegDevice(new Resolution(300));
            final OutputStream imageStream = createImageStream(involvedJpgCalendarPath);
            jpegDevice.process(document.getPages().get_Item(1), imageStream);
        }
        cropImage(involvedJpgCalendarPath.toString());
    }

    private static OutputStream createImageStream(final Path involvedJpgCalendarPath) throws GenerateJpgFromExcelException {
        try {
            return new FileOutputStream(involvedJpgCalendarPath.toFile());
        } catch (final FileNotFoundException e) {
            throw new GenerateJpgFromExcelException(e);
        }
    }

    private static void cropImage(final String filePath) throws GenerateJpgFromExcelException {
        final File imageFile = new File(filePath);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageFile);
            final int imageHeight = bufferedImage.getHeight() - HEIGHT_TO_CROP;
            final int imageWidth = bufferedImage.getWidth();
            final BufferedImage image = bufferedImage.getSubimage(0, HEIGHT_TO_CROP, imageWidth, imageHeight);
            ImageIO.write(image, "jpg", imageFile);
        }
        catch (final IOException e) {
            throw new GenerateJpgFromExcelException(e);
        }
    }
}
