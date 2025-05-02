package com.example.phonebook.web.api;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.dto.CreateContactDTO;
import com.example.phonebook.dto.UpdateContactDTO;
import com.example.phonebook.service.ContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/contacts")
public class ContactApiController {

    private final ModelMapper modelMapper;
    private final ContactService contactService;

    /*
    @GetMapping
    public ResponseEntity<Page<ContactDTO>> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "isFavorite") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<ContactDTO> contactsPage = contactService.getAllContacts(page, size, sortField, sortDir);
        return ResponseEntity.ok(contactsPage);
    }
    */

    @GetMapping
    public Page<ContactDTO> getContacts(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return contactService.getContacts(firstName, lastName, phoneNumber, email, page, size, sortField, sortDir);
    }

    @GetMapping("/{id}")
    public ContactDTO getContactById(@PathVariable long id) {
        return contactService.getContactById(id);
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
