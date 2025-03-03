package com.transports.spring.model.templategeneration.passenger;

import com.transports.spring.dto.generatefiles.DtoTemplateFileDir;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;
import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelPassengerBody;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.templategeneration.common.AbstractTemplateFile;
import com.transports.spring.model.templategeneration.common.EnumTemplateFileDirectory;
import com.transports.spring.model.templategeneration.common.TemplateJpg;
import com.transports.spring.model.templategeneration.common.TemplatePdf;

import java.io.IOException;
import java.nio.file.Path;

public class PassengerTemplateFile extends AbstractTemplateFile {

    private final PassengerTemplateExcel passengerTemplateExcel;

    public PassengerTemplateFile(PassengerTemplateExcel passengerTemplateExcel) {
        super();
        this.passengerTemplateExcel = passengerTemplateExcel;
    }

    public void generate(final DtoTemplateExcelPassengerBody templateExcelPassengerBody, final DtoTemplateExcelHeader dtoHeader, final DtoTemplateFileDir dtoTemplateFileDir) throws IOException, GeneratePdfFromExcelException, GenerateJpgFromExcelException {
        final String fileName = getFileName(dtoHeader);

        final Path excelDir = dtoTemplateFileDir.get(EnumTemplateFileDirectory.EXCEL);
        final Path excelPath = Path.of(excelDir + "/" + fileName + ".xlsx");
        this.passengerTemplateExcel.generate(templateExcelPassengerBody, dtoHeader, excelPath);

        final Path pdfDir = dtoTemplateFileDir.get(EnumTemplateFileDirectory.PDF);
        final Path pdfPath = Path.of(pdfDir + "/" + fileName + ".pdf");
        TemplatePdf.generate(excelPath, pdfPath);

        final Path jpgDir = dtoTemplateFileDir.get(EnumTemplateFileDirectory.JPG);
        final Path jpgPath = Path.of(jpgDir + "/" + fileName + ".jpg");
        TemplateJpg.generate(pdfPath, jpgPath);
    }
}
