package com.transports.spring.controller.stats;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.transports.spring.dto.stats.driver.DtoDriverStats;
import com.transports.spring.service.stats.DriverStatsService;

import java.util.List;

@Controller
@RequestMapping("/stats")
public final class DriverStatsController {

    private final DriverStatsService driverStatsService;

    public DriverStatsController(final DriverStatsService driverStatsService) {
        this.driverStatsService = driverStatsService;
    }

    @GetMapping("/openStats")
    public String openStats(final Model model) {
        
        final List<DtoDriverStats> lastYearDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(12);
        model.addAttribute("lastYearDrivers", lastYearDrivers);

        final List<DtoDriverStats> lastSixMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(6);
        model.addAttribute("lastSixMonthsDrivers", lastSixMonthsDrivers);
        
        final List<DtoDriverStats> lastThreeMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(3);
        model.addAttribute("lastThreeMonthsDrivers", lastThreeMonthsDrivers);

        return "stats/stats";
    }

    
    @GetMapping("/showDriverStats")
    public String showDriverStats(final Model model) {
        final List<DtoDriverStats> lastYearDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(12);
        model.addAttribute("lastYearDrivers", lastYearDrivers);
        
        final List<DtoDriverStats> lastSixMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(6);
        model.addAttribute("lastSixMonthsDrivers", lastSixMonthsDrivers);
        
        final List<DtoDriverStats> lastThreeMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(3);
        model.addAttribute("lastThreeMonthsDrivers", lastThreeMonthsDrivers);

        return "components/driver :: driverStats";
    }
}