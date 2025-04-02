package com.transports.spring.dto.generatefiles;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.model.Driver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGenerateDriverFile {
    private Map<Driver, Map<LocalDate, DtoDriverTransport>> driverTransports;
    private Map<LocalDate, DtoTemplateDate> monthTransportDatesList;
}
