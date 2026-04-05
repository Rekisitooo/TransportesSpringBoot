package com.transports.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/stats")
public final class StatsController {

    public StatsController() {}

    @GetMapping("/openStats")
    public String openStats(final Model model) {
        return "stats";
    }
}