package com.transports.spring.vo.completemodel;

import com.transports.spring.model.Driver;
import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.TransportDateByTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class VoCompleteNotification {
    private Driver driver;
    private Passenger passenger;
    private InvolvedTransportNotification involvedTransportNotification;
    private TransportDateByTemplate transportDateByTemplate;

    public Integer getPassengerId() {
        return ((this.passenger != null) ? this.passenger.getId() : null);
    }

    public Integer getDriverId() {
        return ((this.driver != null) ? this.driver.getId() : null);
    }
}