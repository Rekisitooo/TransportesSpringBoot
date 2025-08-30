package com.transports.spring.vo.driver;

import java.util.List;
import java.util.Map;

import com.transports.spring.model.Driver;
import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Transport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoScreenDriverTransportsTableInfo {

    private List<Integer> templateDateIdList;
    private List<Driver> templateDriverList;
    private Map<Integer, Map<Integer, Transport>> allTemplateDriverTransports;
    private Map<Integer, Map<Integer, InvolvedAvailabiltyForTransportDate>> driversAssistanceDates;
    private Map<Integer, Map<Integer, InvolvedTransportNotification>> driverNotifications;

}
