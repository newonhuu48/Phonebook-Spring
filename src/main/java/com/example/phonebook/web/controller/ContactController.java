package com.example.phonebook.web.controller;

import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.dto.CreateContactDTO;
import com.example.phonebook.dto.UpdateContactDTO;
import com.example.phonebook.service.ContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;
    private final ModelMapper modelMapper;


    @GetMapping
    public String getContacts(Model model) {
        final List<ContactDTO> contacts = contactService.getAllContacts();

        model.addAttribute("contacts", contacts);
        return "list";
    }



    //CREATE CONTACT
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("contact", new ContactDTO());
        return "create";
    }

    @PostMapping("/create")
    public String createContact(@ModelAttribute @Valid CreateContactDTO contactDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("contact", contactDTO);
            return "create";
        }

        contactService.createContact(contactDTO);

        return "redirect:list";
    }



    //EDIT CONTACT
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable long id, Model model) {

        ContactDTO contactDTO = contactService.getContactById(id);

        UpdateContactDTO updateContactDTO = modelMapper.map(contactDTO, UpdateContactDTO.class);
        model.addAttribute("contact", updateContactDTO);

        return "edit";
    }

    @PutMapping("/edit/{id}")
    public String updateContact(@PathVariable long id, @ModelAttribute("contact") @Valid UpdateContactDTO contactDTO, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("contact", contactDTO);
            return "edit";
        }

        contactService.updateContact(id, contactDTO);

        return "redirect:list";
    }



    //DELETE CONTACT
    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable long id, Model model) {
        ContactDTO contactDTO = contactService.getContactById(id);
        model.addAttribute("contact", contactDTO);

        return "delete";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteContact(@PathVariable long id) {
        contactService.deleteContact(id);

        return "redirect:list";
    }
}
