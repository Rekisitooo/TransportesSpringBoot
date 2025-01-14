package com.transports.spring;

import com.transports.spring.controller.TemplateController;
import com.transports.spring.model.Template;
import com.transports.spring.service.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    private final TemplateService templateService;

    public MainController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @RequestMapping("/")
    public String index(final Model model) {
        final List<Template> templateList = this.templateService.getAllWithMonthNames();
        model.addAttribute("templateList", templateList);
        model.addAttribute("username", "Iker Quijano");
        return "index";
    }

    @RequestMapping("enable_javascript")
    public String enableJavascript(final Model model){
        return "enable_javascript";
    }
}
