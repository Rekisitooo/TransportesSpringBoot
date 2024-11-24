package com.transports.spring;

import com.transports.spring.controller.TemplateController;
import com.transports.spring.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private TemplateController templateController;

    @RequestMapping("/")
    public String index(final Model model) {
        final List<Template> templateList = this.templateController.getAllWithMonthNames();
        model.addAttribute("templateList", templateList);
        model.addAttribute("username", "Iker Quijano");
        return "index";
    }

    @RequestMapping("/passengersCRUD")
    public String passengersCRUD(final Model model) {
        final List<Template> templateList = this.templateController.getAllWithMonthNames();
        model.addAttribute("templateList", templateList);
        model.addAttribute("username", "Iker Quijano");
        return "passengerCRUD";
    }
}
