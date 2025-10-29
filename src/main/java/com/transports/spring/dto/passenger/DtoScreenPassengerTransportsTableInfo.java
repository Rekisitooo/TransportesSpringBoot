package com.transports.spring.dto.passenger;

import java.util.List;
import java.util.Map;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoScreenPassengerTransportsTableInfo {
    private List<DtoTemplateDate> templateDateList;
    private List<Passenger> templatePassengerList;
    private Map<Integer, Map<Integer, VoCompleteTransport>> allTemplatePassengerTransports;
    private Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> passengersAssistanceDates;
    private Map<Integer, Map<Integer, VoCompleteNotification>> passengerNotifications;
    private Map<Integer, List<Driver>> driversAvailableByDate;

}
