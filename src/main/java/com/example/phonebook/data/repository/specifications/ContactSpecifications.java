package com.example.phonebook.data.repository.specifications;

import com.example.phonebook.data.entity.Contact;
import org.springframework.data.jpa.domain.Specification;


public class ContactSpecifications {
    public static Specification<Contact> hasFirstName(String firstName) {
        return (root, query, cb) -> cb.equal(root.get("firstName"), firstName);
    }

    public static Specification<Contact> hasLastName(String lastName) {
        return (root, query, cb) -> cb.equal(root.get("lastName"), lastName);
    }

    public static Specification<Contact> hasPhoneNumber(String phoneNumber) {
        return (root, query, cb) -> cb.equal(root.get("phoneNumber"), phoneNumber);
    }

    public static Specification<Contact> hasEmail(String email) {
        return (root, query, cb) -> cb.equal(root.get("email"), email);
    }
}
