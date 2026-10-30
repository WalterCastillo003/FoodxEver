package com.foodxever.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
    @GetMapping("/administracion")
    public String inicio() {
        return "admin/inicio";
    }
}
