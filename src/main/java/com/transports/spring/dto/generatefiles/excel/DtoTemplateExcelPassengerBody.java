package com.transports.spring.dto.generatefiles.excel;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.model.TransportDateByTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoTemplateExcelPassengerBody {
    private List<DtoTemplateDay> passengerAssistanceDateList;
    private Map<LocalDate, TransportDateByTemplate> monthTransportDateByDayMap;
    private Map<LocalDate, DtoPassengerTransport> allTemplatePassengerTransportsByDayMap;
}
