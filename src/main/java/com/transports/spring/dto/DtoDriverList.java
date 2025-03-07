package com.transports.spring.dto;

import com.transports.spring.model.Driver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class DtoDriverList {
    private List<Driver> driversFromTemplateList;
    private int totalDriverAvailableSeats;
}
