package com.transports.spring.model.templategeneration.driver;

import com.transports.spring.dto.generatefiles.DtoTemplateFileDir;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelDriverBody;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.templategeneration.common.EnumTemplateFileDirectory;
import com.transports.spring.model.templategeneration.common.TemplateJpg;
import com.transports.spring.model.templategeneration.common.TemplatePdf;

import java.io.IOException;
import java.nio.file.Path;

public class DriverTemplateFile {

    private final DriverTemplateExcel driverTemplateExcel;

    public DriverTemplateFile(DriverTemplateExcel driverTemplateExcel) {
        this.driverTemplateExcel = driverTemplateExcel;
    }

    public void generate(final DtoTemplateExcelDriverBody dtoTemplateExcelDriverBody, final DtoTemplateExcelHeader dtoHeader, final DtoTemplateFileDir dtoTemplateFileDir) throws IOException, GeneratePdfFromExcelException, GenerateJpgFromExcelException {
        final String driverFullName = dtoHeader.getInvolvedFullName();

        Path excelPath = dtoTemplateFileDir.get(EnumTemplateFileDirectory.EXCEL);
        excelPath = this.driverTemplateExcel.generate(dtoTemplateExcelDriverBody, dtoHeader, excelPath);

        Path pdfPath = dtoTemplateFileDir.get(EnumTemplateFileDirectory.PDF);
        pdfPath = TemplatePdf.generate(excelPath, pdfPath, driverFullName);

        final Path jpgPath = dtoTemplateFileDir.get(EnumTemplateFileDirectory.JPG);
        TemplateJpg.generate(pdfPath, jpgPath, driverFullName);
    }
}
