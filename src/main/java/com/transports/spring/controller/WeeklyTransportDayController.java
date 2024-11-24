package com.transports.spring.controller;

import com.transports.spring.model.WeeklyTransportDay;
import com.transports.spring.repository.IWeeklyTransportDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public final class WeeklyTransportDayController {

    @Autowired
    private IWeeklyTransportDayRepository weeklyTransportDayRepository;

    @GetMapping("/getActiveWeeklyTransportDays")
    public final List<WeeklyTransportDay> getActiveWeeklyTransportDays() {
        return this.weeklyTransportDayRepository.findActiveWeeklyTransportDayList();
    }

    @GetMapping("/getAllWeeklyTransportDays")
    public final List<WeeklyTransportDay> getAllWeeklyTransportDays() {
        return weeklyTransportDayRepository.findAll();
    }

    @GetMapping("/createWeeklyTransportDays")
    public final WeeklyTransportDay createWeeklyTransportDay(@RequestBody final WeeklyTransportDay weeklyTransportDay) {
        return this.weeklyTransportDayRepository.save(weeklyTransportDay);
    }

    @GetMapping("/updateWeeklyTransportDays")
    public final WeeklyTransportDay updatePassenger(@RequestBody final WeeklyTransportDay weeklyTransportDayToUpdate, @PathVariable (value = "id") final int passengerId) {
        final WeeklyTransportDay weeklyTransportDay = this.weeklyTransportDayRepository.findById(passengerId).orElseThrow();
        weeklyTransportDay.setActive(weeklyTransportDayToUpdate.isActive());
        weeklyTransportDay.setDescription(weeklyTransportDayToUpdate.getDescription());
        weeklyTransportDay.setDayOfTheWeek(weeklyTransportDayToUpdate.getDayOfTheWeek());
        return this.weeklyTransportDayRepository.save(weeklyTransportDay);
    }

    /**
     * Does not delete, it mantains it for the history and statistics.
     * @param weeklyTransportDayId
     * @return AbstractInvolved
     */
    @GetMapping("/deleteWeeklyTransportDays")
    public final WeeklyTransportDay deletePassenger(@PathVariable (value = "id") final int weeklyTransportDayId) {
        final WeeklyTransportDay weeklyTransportDay = this.weeklyTransportDayRepository.findById(weeklyTransportDayId).orElseThrow();
        weeklyTransportDay.setActive(false);
        return this.weeklyTransportDayRepository.save(weeklyTransportDay);
    }
}