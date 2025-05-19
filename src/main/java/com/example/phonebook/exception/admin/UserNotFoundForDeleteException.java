package com.example.phonebook.exception.admin;

public class UserNotFoundForDeleteException extends RuntimeException {
    public UserNotFoundForDeleteException(String message) {
        super(message);
    }
}
