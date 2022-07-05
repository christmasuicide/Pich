package com.kma.pich.controller;

import com.kma.pich.type.InvalidRegistrationDataException;
import com.kma.pich.type.UserAlreadyExistAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserAlreadyExistAuthenticationException.class)
    public String handle(final UserAlreadyExistAuthenticationException ex) {
        return "redirect:/register?alreadyExists";
    }

    @ExceptionHandler(InvalidRegistrationDataException.class)
    public String handle(final InvalidRegistrationDataException ex) {
        return "redirect:/register?invalidData";
    }

}
