package com.transports.spring.dto.generatefiles;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGenerateDriverFile {
    private Map<Driver, Map<LocalDate, DtoDriverTransport>> driverTransports;
}
