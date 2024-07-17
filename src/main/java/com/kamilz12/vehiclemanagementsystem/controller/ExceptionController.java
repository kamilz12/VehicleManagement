package com.kamilz12.vehiclemanagementsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Controller
public class ExceptionController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        System.out.println(ex.getMessage());

        return new ResponseEntity<>("An unexpected error occurred. Please try again later.", HttpStatus.NO_CONTENT);
    }

    // Example for handling a custom exception


}