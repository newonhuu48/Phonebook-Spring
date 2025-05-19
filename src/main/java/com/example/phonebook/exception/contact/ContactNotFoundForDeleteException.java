package com.example.phonebook.exception.contact;

public class ContactNotFoundForDeleteException extends RuntimeException {
    public ContactNotFoundForDeleteException(String message) {
        super(message);
    }
}