package com.transports.spring.controller;

import com.transports.spring.controller.passenger_controller.FormDtoGetAllPassengers;
import com.transports.spring.controller.passenger_controller.ProcedureRepository;
import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import com.transports.spring.model.AbstractInvolved;
import com.transports.spring.model.WeeklyTransportDay;
import com.transports.spring.repository.IPassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/passenger")
public final class PassengerController {

    @Autowired
    private IPassengerRepository passengerRepository;

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private WeeklyTransportDayController weeklyTransportDayController;

    @RequestMapping("/Crud")
    public String passengersCrud(final Model model) {
        final List<WeeklyTransportDay> activeWeeklyTransportDays = this.weeklyTransportDayController.getActiveWeeklyTransportDays();
        try {
            final List<DtoGetAllPassengers> passengerList = procedureRepository.getAllPassengers(1, 1);
            final FormDtoGetAllPassengers formDtoGetAllPassengers = new FormDtoGetAllPassengers(passengerList);
            model.addAttribute("FormDtoGetAllPassengers", formDtoGetAllPassengers);
        } catch (final SQLException e) {
            //TODO log
            model.addAttribute("error", true);
        }
        model.addAttribute("activeTransportDays", activeWeeklyTransportDays);
        return "passengerCRUD";
    }

    @GetMapping("/getPassengerById")
    public AbstractInvolved getPassengerById(@PathVariable (value = "id") final int passengerId) {
        return this.passengerRepository.findById(passengerId).orElseThrow();
    }

    @GetMapping("/getAllPassenger")
    public List<DtoGetAllPassengers> getAllPassengers() throws SQLException {
        return procedureRepository.getAllPassengers(1, 1);
    }

    @GetMapping("/createPassenger")
    public AbstractInvolved createPassenger(@RequestBody final AbstractInvolved passenger) {
        return this.passengerRepository.save(passenger);
    }

    @GetMapping("/updatePassenger")
    public AbstractInvolved updatePassenger(@RequestBody final AbstractInvolved passengerToUpdate, @PathVariable (value = "id") final int passengerId) {
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
    public AbstractInvolved deletePassenger(@PathVariable (value = "id") final int passengerId) {
        final AbstractInvolved passenger = this.passengerRepository.findById(passengerId).orElseThrow();
        passenger.setActive(false);
        return this.passengerRepository.save(passenger);
    }
}