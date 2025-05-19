package com.example.phonebook.exception.admin;

public class UserNotFoundForEditException extends RuntimeException {
    public UserNotFoundForEditException(String message) {
        super(message);
    }
}