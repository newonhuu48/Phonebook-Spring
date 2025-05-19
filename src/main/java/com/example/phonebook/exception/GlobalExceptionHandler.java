package com.example.phonebook.exception;

import com.example.phonebook.exception.contact.ContactNotFoundForDeleteException;
import com.example.phonebook.exception.contact.ContactNotFoundForEditException;
import com.example.phonebook.exception.contact.DeleteAccessDeniedException;
import com.example.phonebook.exception.contact.EditAccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    //RESOURCE NOT FOUND EXCEPTIONS
    @ExceptionHandler(ContactNotFoundForEditException.class)
    public String handleContactNotFoundForEdit(ContactNotFoundForEditException ex, Model model) {

        model.addAttribute("error", ex.getMessage());
        return "edit";
    }

    @ExceptionHandler(ContactNotFoundForDeleteException.class)
    public String handleContactNotFoundForDelete(ContactNotFoundForDeleteException ex, Model model) {

        model.addAttribute("error", ex.getMessage());
        return "list";
    }




    //ACCESS DENIED EXCEPTIONS
    @ExceptionHandler(EditAccessDeniedException.class)
    public String handleEditAccessDenied(EditAccessDeniedException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "edit";
    }

    @ExceptionHandler(DeleteAccessDeniedException.class)
    public String handleDeleteAccessDenied(DeleteAccessDeniedException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "delete";
    }




    //GENERAL EXCEPTION HANDLER
    //
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception exception, Model model) {
        model.addAttribute("error", "Something went wrong: " + exception.getMessage());
        return "redirect:/";
    }
}
