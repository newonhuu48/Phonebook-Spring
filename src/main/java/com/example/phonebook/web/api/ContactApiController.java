package com.example.phonebook.web.api;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.dto.CreateContactDTO;
import com.example.phonebook.dto.UpdateContactDTO;
import com.example.phonebook.service.ContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/contacts")
public class ContactApiController {

    private final ModelMapper modelMapper;
    private final ContactService contactService;


    @GetMapping
    public List<ContactDTO> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")
    public ContactDTO getContactById(@PathVariable long id) {
        return contactService.getContactById(id);
    }

    @GetMapping("/first-name/{firstName}")
    public List<ContactDTO> getContactsByFirstName(@PathVariable String firstName) {
        return contactService.getContactsByFirstName(firstName);
    }

    @GetMapping("/last-name/{lastName}")
    public List<ContactDTO> getContactsByLastName(@PathVariable String lastName) {
        return contactService.getContactsByLastName(lastName);
    }

    @GetMapping("/phone-number/{phoneNumber}")
    public List<ContactDTO> getContactsByPhoneNumber(@PathVariable String phoneNumber) {
        return contactService.getContactsByPhoneNumber(phoneNumber);
    }

    @GetMapping("/email/{email}")
    public List<ContactDTO> getContactsByEmail(@PathVariable String email) {
        return contactService.getContactsByEmail(email);
    }


    @PostMapping
    public CreateContactDTO createContact(@Valid @RequestBody CreateContactDTO contactDTO) {
        return contactService.createContact(contactDTO);
    }

    @PutMapping("/{id}")
    public UpdateContactDTO updateContact(@PathVariable long id, @Valid @RequestBody UpdateContactDTO contactDTO) {
        return contactService.updateContact(id, contactDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable long id) {
        contactService.deleteContact(id);
    }
}
