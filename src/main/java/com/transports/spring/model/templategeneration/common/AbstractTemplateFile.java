package com.transports.spring.model.templategeneration.common;

import com.transports.spring.dto.generatefiles.excel.DtoTemplateExcelHeader;

public abstract class AbstractTemplateFile {

    protected AbstractTemplateFile() {}

    /**
     * @return fileName without extension (year_month_involved)
     */
    protected static String getFileName(final DtoTemplateExcelHeader dtoTemplateExcelHeader) {
        final Integer templateYear = dtoTemplateExcelHeader.getTemplateYear();
        final String involvedFullName = dtoTemplateExcelHeader.getInvolvedFullName();
        String monthName = dtoTemplateExcelHeader.getMonthName();
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);

        final String fileName = templateYear + "_" + monthName + "_" + involvedFullName;
        return fileName.replace(" ", "_");
    }
}
