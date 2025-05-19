package com.example.phonebook.exception.contact;

public class ContactNotFoundForEditException extends RuntimeException {
    public ContactNotFoundForEditException(String message) {
        super(message);
    }
}