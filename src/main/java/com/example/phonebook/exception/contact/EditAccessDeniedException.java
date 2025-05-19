package com.example.phonebook.exception.contact;

public class EditAccessDeniedException extends RuntimeException {
    public EditAccessDeniedException(String message) {
        super(message);
    }
}