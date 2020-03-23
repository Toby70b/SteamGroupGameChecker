package com.sggc.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Swagger2Controller {
    @RequestMapping("")
    public String home() {
        return"redirect:/swagger-ui.html";
    }
}
