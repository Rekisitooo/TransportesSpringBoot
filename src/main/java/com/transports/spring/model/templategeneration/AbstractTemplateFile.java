package com.transports.spring.model.templategeneration;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractTemplateFile {

    protected static final String CALENDAR_NAME_FILE = "calendar";
    protected static final String XSL_EXTENSION = ".xlxs";
    protected static final String ORIGINAL_CALENDAR_FILE = CALENDAR_NAME_FILE + XSL_EXTENSION;
    protected static final int START_ROW_DAYS = 3;// Primera columna

    protected final Workbook newExcel;
    protected final FileInputStream fileInputStream;
    protected final Sheet sheet;

    protected AbstractTemplateFile() throws IOException {
        final Path originalCalendarPath = Paths.get(ORIGINAL_CALENDAR_FILE);
        final File originalCalendarFile = originalCalendarPath.getFileName().toFile();
        this.fileInputStream = new FileInputStream(originalCalendarFile);
        this.newExcel = new HSSFWorkbook(fileInputStream);
        this.sheet = newExcel.getSheetAt(0);
    }

}
