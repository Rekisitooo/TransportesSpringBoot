package com.transports.spring.service.stats;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.transports.spring.dto.stats.driver.DtoDriverStats;
import com.transports.spring.model.Transport;
import com.transports.spring.repository.stats.IDriverStatsRepository;

@Service
public class DriverStatsService {

    private final IDriverStatsRepository driverStatsRepository;

    public DriverStatsService(final IDriverStatsRepository driverStatsRepository) {
        this.driverStatsRepository = driverStatsRepository;
    }

    /**
     * Returns a list of DtoDriverStats objects containing the total number of transports for each driver over the last specified number of months.
     * @param months the number of months to look back for transport data
     * @return a list of DtoDriverStats objects with driver statistics
     */
    public List<DtoDriverStats> findTotalDriverTransportsOverLastMonths(final int months) {
        final LocalDate start = LocalDate.now().minusMonths(months - 1).withDayOfMonth(1);
        final java.sql.Date initialDate = java.sql.Date.valueOf(start);
        final java.sql.Date finalDate = java.sql.Date.valueOf(LocalDate.now());

        // Get all driver codes to obtain the total number of transports for each driver
        final List<DtoDriverStats> driverStatsList = this.driverStatsRepository.findAllDriversWithTransportsOnMonths(initialDate, finalDate);

        final List<Integer> driverCodes = new ArrayList<>();
        for (final DtoDriverStats driverStats : driverStatsList) {
            driverCodes.add(driverStats.getDriver().getId());
        }

        // Get the number of transports for each driver over the last specified number of months
        final List<Transport> driverTransportList = this.driverStatsRepository.findAllDriverTransportsOnMonths(initialDate, finalDate, driverCodes);

        final Map<Integer, Integer> driverTransportCountMap = new HashMap<>();
        
        // set the transport percentage for each driver based on the maximum number of transports
        if (driverTransportList != null && !driverTransportList.isEmpty()) {
            int maxTransports = 0;

            // Map the number of transports for each driver
            for (final Transport driverTransport : driverTransportList) {
                final Integer transportCount = driverTransportCountMap.get(driverTransport.getTransportKey().getDriverId());
                
                if (transportCount != null) {
                    driverTransportCountMap.put(driverTransport.getTransportKey().getDriverId(), transportCount + 1);

                    // to obtain the maximum number of transports for any driver, we compare the current transport count with the maximum
                    if (transportCount + 1 > maxTransports) {
                        maxTransports = transportCount + 1;
                    }

                } else {
                    driverTransportCountMap.put(driverTransport.getTransportKey().getDriverId(), 1);
                }
            }

            // Fill the list of DtoDriverStats with the total number of transports and the transport percentage for each driver
            for (final DtoDriverStats driverStats : driverStatsList) {

                // Get the total number of transports for the driver
                final Integer transportCount = driverTransportCountMap.get(driverStats.getDriver().getId());
                if (transportCount != null) {
                    driverStats.setTotalTransports(transportCount.longValue());
                    
                    if (transportCount > 0 && maxTransports > 0) {
                        driverStats.setTransportPercentage((int) ((transportCount * 100) / maxTransports));
                    }  
                }
            }

            driverStatsList.sort((d1, d2) -> Long.compare(d2.getTotalTransports(), d1.getTotalTransports()));
        }

        return driverStatsList;
    }
}
