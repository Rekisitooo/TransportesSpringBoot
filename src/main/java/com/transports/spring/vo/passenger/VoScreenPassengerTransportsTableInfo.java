package com.transports.spring.vo.passenger;

import java.util.List;
import java.util.Map;

import com.transports.spring.model.Driver;
import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.Transport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoScreenPassengerTransportsTableInfo {

    private List<Integer> templateDateIdList;
    private List<Passenger> templatePassengerList;
    private Map<Integer, Map<Integer, Transport>> allTemplatePassengerTransports;
    private Map<Integer, Map<Integer, InvolvedAvailabiltyForTransportDate>> passengersAssistanceDates;
    private Map<Integer, Map<Integer, InvolvedTransportNotification>> passengerNotifications;
    private Map<Integer, Driver> driversAvailableByDate;

}
