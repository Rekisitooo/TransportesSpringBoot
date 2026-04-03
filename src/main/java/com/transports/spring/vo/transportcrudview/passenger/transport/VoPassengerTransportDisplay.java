package com.transports.spring.vo.transportcrudview.passenger.transport;

import java.util.List;
import java.util.stream.Collectors;

import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoPassengerTransportDisplay {
    private Integer selectedDriverId;
    private List<Driver> differentGroupAvailableDrivers;
    private List<Driver> sameGroupAvailableDrivers;

    public void setAvailableDriverList(final List<Driver> availableDriverList, final Passenger passenger) {
        if (availableDriverList != null) {
            this.sameGroupAvailableDrivers = availableDriverList.stream()
                .filter(driver -> passenger.getMinistryGroup().equals(driver.getMinistryGroup()))
                .collect(Collectors.toList());

            this.differentGroupAvailableDrivers = availableDriverList.stream()
                .filter(driver -> !passenger.getMinistryGroup().equals(driver.getMinistryGroup()))
                .collect(Collectors.toList());
        }
    }
}
