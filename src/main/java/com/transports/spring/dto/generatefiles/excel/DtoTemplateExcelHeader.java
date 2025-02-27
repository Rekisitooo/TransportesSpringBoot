package com.transports.spring.dto.generatefiles.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoTemplateExcelHeader {
    private String involvedFullName;
    private String monthName;
    private Integer templateYear;

    public DtoTemplateExcelHeader(final String monthName, final Integer templateYear) {
        this.templateYear = templateYear;
        this.monthName = monthName;
    }
}
