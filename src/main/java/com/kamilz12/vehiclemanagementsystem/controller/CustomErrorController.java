package com.kamilz12.vehiclemanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "errors/access-denied";
    }
}

