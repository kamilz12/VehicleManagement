package com.kamilz12.vehiclemanagementsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {
    @GetMapping("/loginPage")
    public String getLoginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        return "registerAndLogin/login-page";
    }
    @GetMapping("/welcome")
    public String getWelcome() {
        return "index";
    }

}
