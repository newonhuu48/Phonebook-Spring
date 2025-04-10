package com.example.phonebook.service;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.data.repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    private final ModelMapper modelMapper;

    //TODO
    //Change objects to dto later on
    //Make method to dto-fy entities

    public List<Contact> getAllContacts() {
        return contactRepository.findAll().stream()
                .collect(Collectors.toList());
    }


    public Contact getContactById(long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
    }

    public List<Contact> getContactsByFirstName(String firstName) {
        return contactRepository.findByFirstName(firstName).stream()
                .collect(Collectors.toList());
    }

    public List<Contact> getContactsByLastName(String lastName) {
        return contactRepository.findByLastName(lastName).stream()
                .collect(Collectors.toList());
    }

    public List<Contact> getContactsByPhoneNumber(String phoneNumber) {
        return contactRepository.findByPhoneNumber(phoneNumber).stream()
                .collect(Collectors.toList());
    }

    public List<Contact> getContactsByEmail(String email) {
        return contactRepository.findByEmail(email).stream()
                .collect(Collectors.toList());
    }


    //CREATE
    //UPDATE
    //DELETE
    public Contact create(Contact contact) {
        return contactRepository.save(modelMapper.map(contact, Contact.class));
    }


    public Contact updateStudent(long id, Contact contactNew) {
        Contact contact = modelMapper.map(contactNew, Contact.class);
        contact.setId(id);
        return contactRepository.save(contact);
    }


    public void deleteStudent(long id) {
        contactRepository.deleteById(id);
    }

}
