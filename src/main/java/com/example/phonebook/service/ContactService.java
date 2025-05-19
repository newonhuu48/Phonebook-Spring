package com.example.phonebook.service;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.data.entity.User;
import com.example.phonebook.data.repository.ContactRepository;
import com.example.phonebook.data.repository.UserRepository;
import com.example.phonebook.data.repository.specifications.ContactSpecifications;
import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.dto.CreateContactDTO;
import com.example.phonebook.dto.UpdateContactDTO;
import com.example.phonebook.exception.contact.ContactNotFoundForDeleteException;
import com.example.phonebook.exception.contact.ContactNotFoundForEditException;
import com.example.phonebook.exception.contact.DeleteAccessDeniedException;
import com.example.phonebook.exception.contact.EditAccessDeniedException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    //GET CONTACTS OF ALL USERS
    //FOR API USAGE ONLY!!!!!!!
    public Page<ContactDTO> getAllContacts(
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



    public Page<ContactDTO> getContactsByUser(
            String firstName, String lastName, String phoneNumber, String email,
            int page, int size, String sortField, String sortDir) {


        User currentUser = getCurrentUser();

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());


        //Specification<Contact> spec = Specification.where(null);

        //FETCH ONLY CONTACTS OWNED BY USER
        Specification<Contact> spec = ContactSpecifications.belongsToUser(currentUser);


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
    //
    //CREATE CONTACT
    public CreateContactDTO createContact(CreateContactDTO contactDTO) {
        User currentUser = getCurrentUser();  // fetch current user from SecurityContextHolder

        Contact contact = convertToEntity(contactDTO);
        contact.setUser(currentUser);  // assign owner

        Contact savedContact = contactRepository.save(contact);

        return convertToCreateContactDto(savedContact);
    }

    //UPDATE CONTACT
    public UpdateContactDTO updateContact(long id, UpdateContactDTO contactDTO) {

        User currentUser = getCurrentUser();

        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundForEditException("Contact not found"));

        if (!existingContact.getUser().getId().equals(currentUser.getId())) {
            throw new EditAccessDeniedException("You don't have permission to update this contact");
        }

        // update fields (avoid overwriting user or id!)
        existingContact.setFirstName(contactDTO.getFirstName());
        existingContact.setLastName(contactDTO.getLastName());
        existingContact.setPhoneNumber(contactDTO.getPhoneNumber());
        existingContact.setEmail(contactDTO.getEmail());
        existingContact.setFavorite(contactDTO.isFavorite());

        Contact savedContact = contactRepository.save(existingContact);

        return convertToUpdateContactDto(savedContact);
    }

    //DELETE CONTACT
    public void deleteContact(long id) {
        User currentUser = getCurrentUser();

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundForDeleteException("Contact not found"));

        if (!contact.getUser().getId().equals(currentUser.getId())) {
            throw new DeleteAccessDeniedException("You don't have permission to delete this contact");
        }

        contactRepository.delete(contact);
    }




    // HELPER FUNCTIONS
    //
    // GET CURRENTLY LOGGED IN USER
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
