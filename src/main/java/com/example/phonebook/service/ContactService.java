package com.example.phonebook.service;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.data.repository.ContactRepository;
import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.dto.CreateContactDTO;
import com.example.phonebook.dto.UpdateContactDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ModelMapper modelMapper;


    public List<ContactDTO> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();

        //FAVORITES FIRST
        contacts.sort((c1, c2) -> Boolean.compare(c2.isFavorite(), c1.isFavorite()));

        return convertContactsToDtos(contacts);
    }


    public ContactDTO getContactById(long id) {
        return contactRepository.findById(id)
                .map(this::convertToContactDto)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id: " + id));
    }


    public List<ContactDTO> getContactsByFirstName(String firstName) {
        return convertContactsToDtos(contactRepository.findByFirstName(firstName));
    }

    public List<ContactDTO> getContactsByLastName(String lastName) {
        return  convertContactsToDtos(contactRepository.findByLastName(lastName) );
    }

    public List<ContactDTO> getContactsByPhoneNumber(String phoneNumber) {
        return  convertContactsToDtos(contactRepository.findByPhoneNumber(phoneNumber) );
    }

    public List<ContactDTO> getContactsByEmail(String email) {
        return  convertContactsToDtos(contactRepository.findByEmail(email) );
    }


    //CREATE
    //UPDATE
    //DELETE
    public CreateContactDTO createContact(CreateContactDTO contactDTO) {

        Contact contact = convertToEntity(contactDTO);
        Contact savedContact = contactRepository.save(contact);

        return convertToCreateContactDto(savedContact);
    }

    public UpdateContactDTO updateContact(long id, UpdateContactDTO contactDTO) {

        Contact contact = convertToEntity(contactDTO);
        contact.setId(id);

        Contact savedContact = contactRepository.save(contact);

        return convertToUpdateContactDto(savedContact);
    }

    public void deleteContact(long id) {
        contactRepository.deleteById(id);
    }



    //CONVERTER FUNCTION
    //ENTITY LIST -> DTO LIST
    private List<ContactDTO> convertContactsToDtos(List<Contact> contacts) {
        return contacts.stream()
                .map(this::convertToContactDto)
                .collect(Collectors.toList());
    }


    //CONVERTER FUNCTIONS
    //ENTITY -> DTO
    private ContactDTO convertToContactDto(Contact contact) {
        return modelMapper.map(contact, ContactDTO.class);
    }

    private CreateContactDTO convertToCreateContactDto(Contact contact) {
        return modelMapper.map(contact, CreateContactDTO.class);
    }

    private UpdateContactDTO convertToUpdateContactDto(Contact contact) {
        return modelMapper.map(contact, UpdateContactDTO.class);
    }


    //DTO -> ENTITY
    private Contact convertToEntity(ContactDTO contactDto) {
        return modelMapper.map(contactDto, Contact.class);
    }

    private Contact convertToEntity(CreateContactDTO createContactDto) {
        return modelMapper.map(createContactDto, Contact.class);
    }

    private Contact convertToEntity(UpdateContactDTO updateContactDto) {
        return modelMapper.map(updateContactDto, Contact.class);

    }

}
