package com.transports.spring.dto.generatefiles.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoTemplateExcelTransportCellGroup {
    private Integer excelCol;
    private Integer excelRow;
    private XSSFSheet excelSheet;
    private String cellNumberText;
    private String headerText;
    private String bodyText;

    public DtoTemplateExcelTransportCellGroup(Integer excelCol, Integer excelRow, XSSFSheet excelSheet, String cellNumberText) {
        this.excelCol = excelCol;
        this.excelRow = excelRow;
        this.excelSheet = excelSheet;
        this.cellNumberText = cellNumberText;
    }
}
