package com.kamilz12.vehiclemanagementsystem.security;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    public CustomAuthenticationSuccessHandler(UserService theUserService) {
        userService = theUserService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse
            response, Authentication authentication)
            throws IOException {
        System.out.println("In customAuthenticationSuccessHandler");
        String userName = authentication.getName();
        System.out.println("userName=" + userName);
        User theUser = userService.findUserByUsername(userName);
        // now place in the session
        HttpSession session = request.getSession();
        session.setAttribute("user", theUser);
        // forward to home page
        response.sendRedirect(request.getContextPath() + "/");
    }
}
