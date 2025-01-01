package com.transports.spring.controller;

import com.transports.spring.dto.DtoFormGetAllPassengers;
import com.transports.spring.dto.DtoGetAllPassengers;
import com.transports.spring.exception.TransportsException;
import com.transports.spring.model.*;
import com.transports.spring.service.PassengerService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/passenger")
public final class PassengerController {

    private final PassengerService passengerService;
    private final WeeklyTransportDayController weeklyTransportDayController;

    public PassengerController(PassengerService passengerService, WeeklyTransportDayController weeklyTransportDayController) {
        this.passengerService = passengerService;
        this.weeklyTransportDayController = weeklyTransportDayController;
    }

    @RequestMapping("/Crud")
    public String passengersCrud(final Model model, @ModelAttribute DtoGetAllPassengers passengerFilters) {
        final List<WeeklyTransportDay> activeWeeklyTransportDays = this.weeklyTransportDayController.getActiveWeeklyTransportDays();
        try {
            final List<DtoGetAllPassengers> passengerList = this.passengerService.getAllPassengers(1, 1);
            model.addAttribute("DtoFormGetAllPassengers", new DtoFormGetAllPassengers(passengerList));
        } catch (final SQLException e) {
            //TODO log exception
            model.addAttribute("error", true);
        }
        model.addAttribute("activeTransportDays", activeWeeklyTransportDays);
        model.addAttribute("userCode", 1);
        return "passengerCRUD";
    }

    @PostMapping("/updatePassengers")
    public String updatePassengers(final Model model, final DtoFormGetAllPassengers passengersCRUDform) {
        try {
            this.passengerService.updatePassenger(passengersCRUDform);
        } catch (final TransportsException e) {
            //TODO log exception
            model.addAttribute("updateError", true);
        }
        this.passengersCrud(model);
        return "redirect:/passenger/Crud";
    }


    @GetMapping("/getAllPassenger")
    public List<DtoGetAllPassengers> getAllPassengers() throws SQLException {
        return this.passengerService.getAllPassengers(1, 1);
    }
}