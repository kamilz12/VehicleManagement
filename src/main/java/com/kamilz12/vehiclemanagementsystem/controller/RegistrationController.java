package com.kamilz12.vehiclemanagementsystem.controller;

import java.util.logging.Logger;

import com.kamilz12.vehiclemanagementsystem.dto.UserDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	private final Logger logger = Logger.getLogger(getClass().getName());

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


	@GetMapping("/showRegistrationForm")
	public String showMyLoginPage(Model theModel) {
		
		theModel.addAttribute("webUser", new UserDTO());
		
		return "registerAndLogin/registration-form";
	}

	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(
			@Valid @ModelAttribute("webUser") UserDTO userDTO,
			BindingResult theBindingResult,
			HttpSession session, Model theModel) {

		String userName = userDTO.getUsername();
		logger.info("Processing registration form for: " + userName);
		
		// form validation
		 if (theBindingResult.hasErrors()){
			 return "registerAndLogin/registration-form";
		 }

		// check the database if user already exists
        User existing = userService.findUserByUsername(userName);
        if (existing != null){
        	theModel.addAttribute("webUser", new UserDTO());
			theModel.addAttribute("registrationError", "User name already exists.");

			logger.warning("User name already exists.");
        	return "registerAndLogin/registration-form";
        }
        
        // create user account and store in the databse
        userService.save(userDTO);
        
        logger.info("Successfully created user: " + userName);

		// place user in the web http session for later use
		session.setAttribute("user", userDTO);

        return "registerAndLogin/registration-confirmation";
	}
}
