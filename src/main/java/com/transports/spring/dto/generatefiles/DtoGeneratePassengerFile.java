package com.transports.spring.dto.generatefiles;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.dto.DtoTransportDateByTemplate;
import com.transports.spring.model.Event;
import com.transports.spring.model.Passenger;
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
public class DtoGeneratePassengerFile {
    private Map<Passenger, Map<LocalDate, DtoPassengerTransport>> passengerTransports;
    private Map<LocalDate, DtoTransportDateByTemplate> monthTransportDatesList;
    private Map<Integer, List<DtoTemplateDay>> allPassengersAssistanceDatesMap;
}
