package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.dto.UserDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Role;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Checking if admin user exists (delayed init)");

        try {
            User existingAdmin = userService.findUserByUsername("adminek");

            Role adminRole = userService.getRoleRepository().findRoleByName("ROLE_ADMIN");
            if (adminRole == null) {
                log.info("Admin role not found. Creating ROLE_ADMIN...");
                userService.getRoleRepository().addRole("ROLE_ADMIN");
                adminRole = userService.getRoleRepository().findRoleByName("ROLE_ADMIN");
                log.info("Admin role created");
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

                log.info("Admin user created with ROLE_ADMIN");
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

        } catch (Exception e) {
            log.error("Failed to seed admin user: {}", e.getMessage(), e);
        }
    }
}
