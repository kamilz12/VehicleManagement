package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.dto.UserDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Role;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.service.user.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    public void seedAdminUser() {
        log.info("Checking if admin user exists");
        User existingAdmin = userService.findUserByUsername("adminek");

        Role adminRole = userService.getRoleRepository().findRoleByName("ROLE_ADMIN");
        if (adminRole == null) {
            log.info("Admin role not found. Creating ROLE_ADMIN...");
            userService.getRoleRepository().addRole("ROLE_ADMIN");
            adminRole = userService.getRoleRepository().findRoleByName("ROLE_ADMIN");
            log.info("Admin role created successfully");
        }

        if (existingAdmin == null) {
            log.info("Admin user not found. Creating admin user...");
            UserDTO adminUserDTO = new UserDTO("adminek", "admin123");
            userService.save(adminUserDTO);

            User adminUser = userService.findUserByUsername("adminek");
            List<Role> roles = new ArrayList<>(adminUser.getRoles());
            roles.add(adminRole);
            adminUser.setRoles(roles);
            userService.getUserRepository().save(adminUser);

            log.info("Admin user created successfully with ROLE_ADMIN");
        } else {
            log.info("Admin user already exists, ensuring it has ROLE_ADMIN");
            boolean hasAdminRole = existingAdmin.getRoles().stream()
                    .anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()));

            if (!hasAdminRole) {
                List<Role> roles = new ArrayList<>(existingAdmin.getRoles());
                roles.add(adminRole);
                existingAdmin.setRoles(roles);
                userService.getUserRepository().save(existingAdmin);
                log.info("ROLE_ADMIN added to existing admin user");
            } else {
                log.info("Admin user already has ROLE_ADMIN");
            }
        }
    }

    @GetMapping("/showRegistrationForm")
    public String showMyLoginPage(Model theModel) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        theModel.addAttribute("user", new UserDTO());
        return "registerAndLogin/registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult theBindingResult, HttpSession session, Model theModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        String userName = userDTO.getUsername();
        log.info("Processing registration form for: {}", userName);

        // form validation
        if (theBindingResult.hasErrors()) {
            return "registerAndLogin/registration-form";
        }

        // check the database if user already exists
        User existing = userService.findUserByUsername(userName);
        if (existing != null) {
            theModel.addAttribute("webUser", new UserDTO());
            theModel.addAttribute("registrationError", "User name already exists.");

            log.warn("User name already exists.");
            return "registerAndLogin/registration-form";
        }
        userService.save(userDTO);

        log.info("Successfully created user: {}", userName);

        session.setAttribute("user", userDTO);

        return "registerAndLogin/registration-confirmation";
    }
}
