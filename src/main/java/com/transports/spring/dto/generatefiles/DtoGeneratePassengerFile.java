package com.transports.spring.dto.generatefiles;

import java.time.LocalDate;
import java.util.Map;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.model.Passenger;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGeneratePassengerFile {
    private Map<Passenger, Map<LocalDate, DtoPassengerTransport>> passengerTransports;
    private Map<LocalDate, DtoTemplateDate> monthTransportDatesList;
    private Map<Integer, Map<LocalDate, VoCompleteInvolvedAvailability>> allPassengersAssistanceDatesMap;
}
