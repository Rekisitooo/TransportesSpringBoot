package com.transports.spring.dto.generatefiles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGenerateFile {
    private DtoGeneratePassengerFile dtoGeneratePassengerFile;
    private DtoGenerateDriverFile dtoGenerateDriverFile;
    private Path monthTempDirPath;
    private Integer templateMonth;
    private Calendar monthCalendar;
    private DtoTemplateFileDir dtoTemplateFileDir;

    public DtoGenerateFile(DtoGeneratePassengerFile dtoGeneratePassengerFile, DtoGenerateDriverFile dtoGenerateDriverFile, Path monthTempDirPath, Integer templateMonth, Calendar monthCalendar) {
        this.dtoGeneratePassengerFile = dtoGeneratePassengerFile;
        this.dtoGenerateDriverFile = dtoGenerateDriverFile;
        this.monthTempDirPath = monthTempDirPath;
        this.templateMonth = templateMonth;
        this.monthCalendar = monthCalendar;
    }
}
