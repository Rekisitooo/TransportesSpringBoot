package com.transports.spring.vo.transportcrudview.passenger.transport;

import java.util.List;

import com.transports.spring.model.Driver;

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
    private List<Driver> availableDriverList;
}
