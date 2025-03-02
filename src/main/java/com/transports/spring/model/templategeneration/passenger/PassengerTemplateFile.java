package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.generatefiles.DtoTemplateFileDir;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.templategeneration.common.EnumTemplateFileDirectory;
import com.transports.spring.model.templategeneration.common.TemplateJpg;
import com.transports.spring.model.templategeneration.common.TemplatePdf;

import java.io.IOException;
import java.nio.file.Path;

public class PassengerTemplateFile {

    private final PassengerTemplateExcel passengerTemplateExcel;

    public PassengerTemplateFile(PassengerTemplateExcel passengerTemplateExcel) {
        super();
        this.passengerTemplateExcel = passengerTemplateExcel;
    }

    public void generate(final DtoTemplateExcelPassengerBody templateExcelPassengerBody, final DtoTemplateExcelHeader dtoHeader, final DtoTemplateFileDir dtoTemplateFileDir) throws IOException, GeneratePdfFromExcelException, GenerateJpgFromExcelException {
        final String passengerFullName = dtoHeader.getInvolvedFullName();

        Path excelPath = dtoTemplateFileDir.get(EnumTemplateFileDirectory.EXCEL);
        excelPath = this.passengerTemplateExcel.generate(templateExcelPassengerBody, dtoHeader, excelPath);

        Path pdfPath = dtoTemplateFileDir.get(EnumTemplateFileDirectory.PDF);
        pdfPath = TemplatePdf.generate(excelPath, pdfPath, passengerFullName);

        final Path jpgPath = dtoTemplateFileDir.get(EnumTemplateFileDirectory.JPG);
        TemplateJpg.generate(pdfPath, jpgPath, passengerFullName);
    }
}
