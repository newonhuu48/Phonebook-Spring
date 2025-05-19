package com.example.phonebook.web.controller;

import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.dto.CreateContactDTO;
import com.example.phonebook.dto.UpdateContactDTO;
import com.example.phonebook.service.ContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;
    private final ModelMapper modelMapper;


    @GetMapping
    public String getContacts(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "isFavorite") String sortField,
            @RequestParam(defaultValue = "dsc") String sortDir,
            Model model) {


        // Call the service to get contacts based on the criteria
        Page<ContactDTO> contactsPage = contactService.getContactsByUser(firstName, lastName, phoneNumber,email, page, size, sortField, sortDir);

        // Add the necessary attributes to the model
        model.addAttribute("contactsPage", contactsPage);
        
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contactsPage.getTotalPages());
        model.addAttribute("totalItems", contactsPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "list"; // Return the view name
    }



    //CREATE CONTACT
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("contact", new CreateContactDTO());
        return "create";
    }

    @PostMapping("/create")
    public String createContact(@ModelAttribute @Valid CreateContactDTO contactDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("contact", contactDTO);
            return "create";
        }

        contactService.createContact(contactDTO);

        return "redirect:/contacts";
    }



    //EDIT CONTACT
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable long id, Model model) {

            ContactDTO contactDTO = contactService.getContactById(id);
            UpdateContactDTO updateContactDTO = modelMapper.map(contactDTO, UpdateContactDTO.class);
            model.addAttribute("contact", updateContactDTO);
            return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateContact(@PathVariable long id, @ModelAttribute("contact") @Valid UpdateContactDTO contactDTO, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("contact", contactDTO);
            return "edit";
        }

        contactService.updateContact(id, contactDTO);

        return "redirect:/contacts";
    }



    //DELETE CONTACT
    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable long id, Model model) {
        ContactDTO contactDTO = contactService.getContactById(id);
        model.addAttribute("contact", contactDTO);

        return "delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteContact(@PathVariable long id) {
        contactService.deleteContact(id);

        return "redirect:/contacts";
    }
}



