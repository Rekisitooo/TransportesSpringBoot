package com.transports.spring.controller.stats;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.transports.spring.dto.stats.driver.DtoDriverStats;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.service.TransportDateByTemplateService;
import com.transports.spring.service.stats.DriverStatsService;

@Controller
@RequestMapping("/stats")
public final class DriverStatsController {

    private final DriverStatsService driverStatsService;
    private final TransportDateByTemplateService transportDateByTemplateService;

    public DriverStatsController(final DriverStatsService driverStatsService, final TransportDateByTemplateService transportDateByTemplateService) {
        this.driverStatsService = driverStatsService;
        this.transportDateByTemplateService = transportDateByTemplateService;
    }

    @GetMapping("/openStats")
    public String openStats(final Model model) {
        
        final List<DtoDriverStats> lastYearDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(12);
        model.addAttribute("lastYearDrivers", lastYearDrivers);

        final List<TransportDateByTemplate> transportDatesOnLastYear = this.transportDateByTemplateService.findTransportsDatesOnMonths(12);
        model.addAttribute("transportDatesOnLastYear", transportDatesOnLastYear.size());

        final List<DtoDriverStats> lastNineMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(9);
        model.addAttribute("lastNineMonthsDrivers", lastNineMonthsDrivers);

        final List<TransportDateByTemplate> transportDatesOnLastNineMonths = this.transportDateByTemplateService.findTransportsDatesOnMonths(9);
        model.addAttribute("transportDatesOnLastNineMonths", transportDatesOnLastNineMonths.size());

        final List<DtoDriverStats> lastSixMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(6);
        model.addAttribute("lastSixMonthsDrivers", lastSixMonthsDrivers);

        final List<TransportDateByTemplate> transportDatesOnLastSixMonths = this.transportDateByTemplateService.findTransportsDatesOnMonths(6);
        model.addAttribute("transportDatesOnLastSixMonths", transportDatesOnLastSixMonths.size());

        final List<DtoDriverStats> lastThreeMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(1);
        model.addAttribute("lastThreeMonthsDrivers", lastThreeMonthsDrivers);

        final List<TransportDateByTemplate> transportDatesOnLastThreeMonths = this.transportDateByTemplateService.findTransportsDatesOnMonths(1);
        model.addAttribute("transportDatesOnLastThreeMonths", transportDatesOnLastThreeMonths.size());

        return "stats/stats";
    }

    
    @GetMapping("/showDriverStats")
    public String showDriverStats(final Model model) {
        final List<DtoDriverStats> lastYearDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(12);
        model.addAttribute("lastYearDrivers", lastYearDrivers);
        
        final List<DtoDriverStats> lastNineMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(9);
        model.addAttribute("lastNineMonthsDrivers", lastNineMonthsDrivers);

        final List<DtoDriverStats> lastSixMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(6);
        model.addAttribute("lastSixMonthsDrivers", lastSixMonthsDrivers);
        
        final List<DtoDriverStats> lastThreeMonthsDrivers = this.driverStatsService.findTotalDriverTransportsOverLastMonths(3);
        model.addAttribute("lastThreeMonthsDrivers", lastThreeMonthsDrivers);

        return "components/driver :: driverStats";
    }
}