package com.example.phonebook.exception.contact;

public class DeleteAccessDeniedException extends RuntimeException {
    public DeleteAccessDeniedException(String message) {
        super(message);
    }
}
