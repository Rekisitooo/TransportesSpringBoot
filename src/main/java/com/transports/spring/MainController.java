package com.transports.spring;

import com.transports.spring.controller.PassengerController;
import com.transports.spring.controller.TemplateController;
import com.transports.spring.controller.WeeklyTransportDayController;
import com.transports.spring.controller.passenger_controller.FormDtoGetAllPassengers;
import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import com.transports.spring.model.Template;
import com.transports.spring.model.WeeklyTransportDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private TemplateController templateController;

    @Autowired
    private WeeklyTransportDayController weeklyTransportDayController;

    @Autowired
    private PassengerController passengerController;

    @RequestMapping("/")
    public String index(final Model model) {
        final List<Template> templateList = this.templateController.getAllWithMonthNames();
        model.addAttribute("templateList", templateList);
        model.addAttribute("username", "Iker Quijano");
        return "index";
    }

    @RequestMapping("/passengersCrud")
    public String passengersCrud(final Model model) {
        final List<WeeklyTransportDay> activeWeeklyTransportDays = this.weeklyTransportDayController.getActiveWeeklyTransportDays();
        try {
            final List<DtoGetAllPassengers> passengerList = passengerController.getAllPassengers();
            final FormDtoGetAllPassengers formDtoGetAllPassengers = new FormDtoGetAllPassengers(passengerList);
            model.addAttribute("FormDtoGetAllPassengers", formDtoGetAllPassengers);
        } catch (final SQLException e) {
            //TODO log
            model.addAttribute("error", true);
        }
        model.addAttribute("activeTransportDays", activeWeeklyTransportDays);
        return "passengerCRUD";
    }
}
