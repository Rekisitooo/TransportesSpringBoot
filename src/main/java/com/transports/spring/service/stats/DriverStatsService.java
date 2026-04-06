package com.transports.spring.service.stats;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.transports.spring.dto.stats.driver.DtoDriverStats;
import com.transports.spring.model.Driver;
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
        final LocalDate start = LocalDate.now().minusMonths(months).withDayOfMonth(1);
        final java.sql.Date initialDate = java.sql.Date.valueOf(start);

        final List<Driver> driverList = this.driverStatsRepository.findAllDriversWithTransportsOnTheLastMonths(initialDate);

        final List<Integer> driverCodes = new ArrayList<>();
        for (final Driver driver : driverList) {
            driverCodes.add(driver.getId());
        }

        final List<DtoDriverStats> driverStatsList = this.driverStatsRepository.findAllDriverTransportsOnTheLastMonths(initialDate, driverCodes);

        // set the transport percentage for each driver based on the maximum number of transports
        if (driverStatsList != null && !driverStatsList.isEmpty()) {
            final Long maxTransports = driverStatsList.get(0).getTotalTransports();
            for (final DtoDriverStats driverStats : driverStatsList) {
                final int transportPercentage = (int) ((driverStats.getTotalTransports() / (double) maxTransports) * 100);
                driverStats.setTransportPercentage(transportPercentage);
            }
        }

        return driverStatsList;
    }
}
