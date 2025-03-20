package com.transports.spring.dto.generatefiles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGenerateFile {
    private DtoGeneratePassengerFile dtoGeneratePassengerFile;
    private DtoGenerateDriverFile dtoGenerateDriverFile;
    private Integer templateMonth;
    private Calendar monthCalendar;
}
