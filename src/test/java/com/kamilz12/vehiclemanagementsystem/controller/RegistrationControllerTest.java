package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.dto.UserDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private RegistrationController registrationController;

    public RegistrationControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showRegistrationForm_ReturnsRegistrationForm() {
        String viewName = registrationController.showMyLoginPage(model);
        assertEquals("registerAndLogin/registration-form", viewName);
        verify(model).addAttribute(eq("user"), any(UserDTO.class));
    }

    @Test
    void processRegistrationForm_WithErrors_ReturnsRegistrationForm() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = registrationController.processRegistrationForm(new UserDTO(), bindingResult, session, model);
        assertEquals("registerAndLogin/registration-form", viewName);
    }

    @Test
    void processRegistrationForm_UserAlreadyExists_ReturnsRegistrationForm() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existingUser");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findUserByUsername("existingUser")).thenReturn(new User());
        String viewName = registrationController.processRegistrationForm(userDTO, bindingResult, session, model);
        assertEquals("registerAndLogin/registration-form", viewName);
        verify(model).addAttribute(eq("registrationError"), eq("User name already exists."));
    }

    @Test
    void processRegistrationForm_SuccessfulRegistration_ReturnsConfirmation() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findUserByUsername("newUser")).thenReturn(null);
        String viewName = registrationController.processRegistrationForm(userDTO, bindingResult, session, model);
        assertEquals("registerAndLogin/registration-confirmation", viewName);
        verify(userService).save(userDTO);
        verify(session).setAttribute(eq("user"), eq(userDTO));
    }
}