package com.example.phonebook.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception exception, Model model) {
        model.addAttribute("errorMessage", "Something went wrong: " + exception.getMessage());
        return "redirect:/contacts";
    }
}
