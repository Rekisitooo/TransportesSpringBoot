package com.transports.spring.controller;

import com.transports.spring.controller.passenger_controller.GetAllPassengers;
import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import com.transports.spring.model.AbstractInvolved;
import com.transports.spring.model.WeeklyTransportDay;
import com.transports.spring.repository.IPassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public final class PassengerController {

    @Autowired
    private IPassengerRepository passengerRepository;

    @GetMapping("/getPassengerById")
    public final AbstractInvolved getPassengerById(@PathVariable (value = "id") final int passengerId) {
        return this.passengerRepository.findById(passengerId).orElseThrow();
    }

    @GetMapping("/getAllPassenger")
    public final List<DtoGetAllPassengers> getAllPassengers() {
        final WeeklyTransportDayController weeklyTransportDayController = new WeeklyTransportDayController();
        final List<WeeklyTransportDay> activeWeeklyTransportDays = weeklyTransportDayController.getActiveWeeklyTransportDays();
        return GetAllPassengers.getOnlyActive(activeWeeklyTransportDays);
    }

    @GetMapping("/createPassenger")
    public final AbstractInvolved createPassenger(@RequestBody final AbstractInvolved passenger) {
        return this.passengerRepository.save(passenger);
    }

    @GetMapping("/updatePassenger")
    public final AbstractInvolved updatePassenger(@RequestBody final AbstractInvolved passengerToUpdate, @PathVariable (value = "id") final int passengerId) {
        final AbstractInvolved passenger = this.passengerRepository.findById(passengerId).orElseThrow();
        passenger.setActive(passengerToUpdate.isActive());
        passenger.setName(passengerToUpdate.getName());
        passenger.setSurname(passengerToUpdate.getSurname());
        return this.passengerRepository.save(passenger);
    }

    /**
     * Does not delete the passenger, it mantains it for the history and statistics.
     * @param passengerId
     * @return AbstractInvolved
     */
    @GetMapping("/deletePassenger")
    public final AbstractInvolved deletePassenger(@PathVariable (value = "id") final int passengerId) {
        final AbstractInvolved passenger = this.passengerRepository.findById(passengerId).orElseThrow();
        passenger.setActive(false);
        return this.passengerRepository.save(passenger);
    }
}