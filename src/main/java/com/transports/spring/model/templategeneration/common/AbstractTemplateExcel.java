package com.transports.spring.model.templategeneration.common;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class AbstractTemplateExcel {

    protected static final String CALENDAR_NAME_FILE = "calendar";
    protected static final String XSLX_EXTENSION = ".xlsx";
    protected static final String ORIGINAL_CALENDAR_FILE_STRING = CALENDAR_NAME_FILE + XSLX_EXTENSION;

    public static final Path ORIGINAL_CALENDAR_PATH = Paths.get(ORIGINAL_CALENDAR_FILE_STRING);
    public static final File ORIGINAL_CALENDAR_FILE = ORIGINAL_CALENDAR_PATH.getFileName().toFile();

    protected static final int START_ROW_DAYS = 0;

    protected final AbstractTemplateExcelHeader templateExcelHeader;
    protected final XSSFWorkbook newExcel;
    protected final FileInputStream fisOriginalCalendar;
    protected final XSSFSheet sheet;

    protected AbstractTemplateExcel(AbstractTemplateExcelHeader templateExcelHeader) throws IOException {
        this.fisOriginalCalendar = new FileInputStream(ORIGINAL_CALENDAR_FILE);
        this.newExcel = new XSSFWorkbook(fisOriginalCalendar);
        this.sheet = this.newExcel.getSheetAt(0);

        this.templateExcelHeader = templateExcelHeader.init(this.sheet, START_ROW_DAYS);
    }

    protected void writeInExcel(final File passengerCalendar) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(passengerCalendar);
        this.newExcel.write(fileOutputStream);
        fileOutputStream.close();
        this.newExcel.close();
        this.fisOriginalCalendar.close();
    }

    /**
     * Creates a temporary file from an existing file.
     *
     * @return the path to the newly created temporary file
     * @throws IOException if an I/O error occurs
     */
    protected static Path createTempInvolvedExcelFromExisting(final String involvedFullName, final Path excelPath) throws IOException {
        final Path tempFile = Files.createTempFile(involvedFullName, XSLX_EXTENSION);
        Files.copy(excelPath, tempFile, StandardCopyOption.REPLACE_EXISTING);

        return tempFile;
    }
}
