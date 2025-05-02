package com.example.phonebook.service;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.data.repository.ContactRepository;
import com.example.phonebook.data.repository.specifications.ContactSpecifications;
import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.dto.CreateContactDTO;
import com.example.phonebook.dto.UpdateContactDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ModelMapper modelMapper;


    public Page<ContactDTO> getContacts(
            String firstName, String lastName, String phoneNumber, String email,
            int page, int size, String sortField, String sortDir) {


        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());


        Specification<Contact> spec = Specification.where(null);

        //ADDING FIELDS TO SPECIFICATION
        //FOR CRITERIA QUERY
        if(firstName != null && !firstName.isBlank() ) {
            spec = spec.and(ContactSpecifications.hasFirstName(firstName));
        }
        if(lastName != null && !lastName.isBlank() ) {
            spec = spec.and(ContactSpecifications.hasLastName(lastName));
        }
        if(phoneNumber != null && !phoneNumber.isBlank()) {
            spec = spec.and(ContactSpecifications.hasPhoneNumber(phoneNumber));
        }
        if(email != null && !email.isBlank()) {
            spec = spec.and(ContactSpecifications.hasEmail(email));
        }

        Page<Contact> contactsPage = contactRepository.findAll(spec, pageable);

        return contactsPage.map(contact -> modelMapper.map(contact, ContactDTO.class));
    }



    public ContactDTO getContactById(long id) {
        return contactRepository.findById(id)
                .map(this::convertToContactDto)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id: " + id));
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
