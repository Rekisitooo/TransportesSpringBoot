package com.transports.spring.dto.generatefiles;

import com.transports.spring.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGenerateFile {
    private DtoGeneratePassengerFile dtoGeneratePassengerFile;
    private DtoGenerateDriverFile dtoGenerateDriverFile;
    private Integer templateMonth;
    private Calendar monthCalendar;
    private Map<LocalDate, Event> dateEventMap;
}
