package com.transports.spring.dto.generatefiles.excel;

import com.transports.spring.dto.DtoDriverTransport;
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
public class DtoTemplateExcelDriverBody {
    private Map<LocalDate, DtoDriverTransport> driverTransportForDayMap;
}
