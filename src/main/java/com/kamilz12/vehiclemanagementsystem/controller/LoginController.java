package com.kamilz12.vehiclemanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {
    @GetMapping("/loginPage")
    public String getLoginPage() {
        return "registerAndLogin/login-page";
    }
    @GetMapping("/welcome")
    public String getWelcome() {
        return "welcome";
    }

}
