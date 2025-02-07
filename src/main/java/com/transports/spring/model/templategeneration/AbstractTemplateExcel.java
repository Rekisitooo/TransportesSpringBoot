package com.transports.spring.model.templategeneration;

import com.transports.spring.dto.DtoInvolvedTransport;
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
import java.util.List;

public abstract class AbstractTemplateExcel {

    protected static final String CALENDAR_NAME_FILE = "calendar";
    protected static final String XSL_EXTENSION = ".xlsx";
    protected static final String ORIGINAL_CALENDAR_FILE_STRING = CALENDAR_NAME_FILE + XSL_EXTENSION;

    public static final Path ORIGINAL_CALENDAR_PATH = Paths.get(ORIGINAL_CALENDAR_FILE_STRING);
    public static final File ORIGINAL_CALENDAR_FILE = ORIGINAL_CALENDAR_PATH.getFileName().toFile();

    protected static final int START_ROW_DAYS = 1;

    protected final AbstractTemplateExcelBody templateExcelBody;
    protected final AbstractTemplateExcelHeader templateExcelHeader;
    protected final XSSFWorkbook newExcel;
    protected final FileInputStream fileInputStream;
    protected final XSSFSheet sheet;

    protected AbstractTemplateExcel(AbstractTemplateExcelBody templateExcelBody, AbstractTemplateExcelHeader templateExcelHeader) throws IOException {
        this.fileInputStream = new FileInputStream(ORIGINAL_CALENDAR_FILE);
        this.newExcel = new XSSFWorkbook(fileInputStream);
        this.sheet = this.newExcel.getSheetAt(0);

        this.templateExcelBody = templateExcelBody;
        this.templateExcelHeader = templateExcelHeader.init(this.sheet, START_ROW_DAYS);
    }

    protected Path generate(List<DtoInvolvedTransport> allInvolvedTransportsFromTemplate, String involvedFullName, String monthName, int templateYear) throws IOException {
        this.templateExcelHeader.generate(involvedFullName, monthName, templateYear);
        this.templateExcelBody.generate(this.sheet, allInvolvedTransportsFromTemplate);

        final Path involvedExcelCalendar = createTempInvolvedExcelFromExisting(involvedFullName);
        this.writeInExcel(involvedExcelCalendar);

        return involvedExcelCalendar;
    }

    private void writeInExcel(final Path tempPassengerCalendar) throws IOException {
        final File passengerCalendar = tempPassengerCalendar.getFileName().toFile();
        final FileOutputStream fileOutputStream = new FileOutputStream(passengerCalendar);
        this.newExcel.write(fileOutputStream);
        fileOutputStream.close();
        this.newExcel.close();
        this.fileInputStream.close();
    }

    /**
     * Creates a temporary file from an existing file.
     *
     * @return the path to the newly created temporary file
     * @throws IOException if an I/O error occurs
     */
    private static Path createTempInvolvedExcelFromExisting(final String involvedFullName) throws IOException {
        final Path tempFile = Files.createTempFile(involvedFullName, ".xlsx");
        Files.copy(ORIGINAL_CALENDAR_PATH, tempFile, StandardCopyOption.REPLACE_EXISTING);
        //Files.copy(tempFile, );

        return tempFile;
    }
}
