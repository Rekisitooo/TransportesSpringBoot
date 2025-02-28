package com.transports.spring.dto.generatefiles;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGenerateFile {
    private Map<Passenger, List<DtoPassengerTransport>> passengerTransports;
    private Map<Driver, List<DtoDriverTransport>> driverTransports;
    private Path monthTempDirPath;
    private Integer templateMonth;
    private Calendar monthCalendar;
}
