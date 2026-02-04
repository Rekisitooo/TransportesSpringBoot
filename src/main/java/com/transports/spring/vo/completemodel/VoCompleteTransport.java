package com.transports.spring.vo.completemodel;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.Transport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoCompleteTransport {
    private Driver driver;
    private Passenger passenger;
    private DtoTemplateDate dtoTemplateDate;
    private Transport transport;

    public VoCompleteTransport(Driver driver, Passenger passenger, Transport transport, DtoTemplateDate dtoTemplateDate) {
        this.driver = driver;
        this.passenger = passenger;
        this.dtoTemplateDate = dtoTemplateDate;
        this.transport = transport;
    }

    public Integer getPassengerId() {
        return ((this.passenger != null) ? this.passenger.getId() : null);
    }

    public Integer getDriverId() {
        return ((this.driver != null) ? this.driver.getId() : null);
    }
}