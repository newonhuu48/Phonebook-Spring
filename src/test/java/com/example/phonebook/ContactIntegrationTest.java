package com.example.phonebook;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.data.repository.ContactRepository;
import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.dto.CreateContactDTO;
import com.example.phonebook.dto.UpdateContactDTO;
import com.example.phonebook.service.ContactService;
import com.example.phonebook.web.controller.ContactController;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest //This loads the whole Spring not just Repository or just Controller
@Transactional // Ensures changes are rolled back after each test, keeping the database clean
@AutoConfigureMockMvc
public class ContactIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void testCreateContact() throws Exception {
        mockMvc.perform(post("/contacts/create")
                .param("firstName", "Alice")
                .param("lastName", "Smith")
                .param("phoneNumber", "1234567890")
                .param("email", "alice@example.com")
                .param("favorite", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));

        List<Contact> contacts = contactRepository.findAll();
        assertFalse(contacts.isEmpty());
        Contact saved = contacts.get(0);
        assertEquals("Alice", saved.getFirstName());
    }

    @Test
    void testReadContact() throws Exception {
        // Arrange: Save a contact to be retrieved
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("john@example.com");
        contact.setPhoneNumber("1234567890");
        contact.setFavorite(false);
        contactRepository.save(contact);

        // Act & Assert: Perform GET /contacts and check result
        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attribute("contacts", hasItem(
                        allOf(
                                hasProperty("firstName", is("John")),
                                hasProperty("lastName", is("Doe")),
                                hasProperty("email", is("john@example.com")),
                                hasProperty("phoneNumber", is("1234567890")),
                                hasProperty("favorite", is(false))
                        )
                )));
    }



    @Test
    void testUpdateContact() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("Tom");
        contact.setLastName("Brown");
        contact.setPhoneNumber("111111111");
        contact.setEmail("tom@example.com");
        contact.setFavorite(false);
        Contact saved = contactRepository.save(contact);

        mockMvc.perform(post("/contacts/edit/" + saved.getId())
                        .param("firstName", "Thomas")
                        .param("lastName", "Brown")
                        .param("phoneNumber", "222222222")
                        .param("email", "thomas@example.com")
                        .param("favorite", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));

        Contact updated = contactRepository.findById(saved.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals("Thomas", updated.getFirstName());
        assertEquals("222222222", updated.getPhoneNumber());
        assertEquals("thomas@example.com", updated.getEmail());
        assertTrue(updated.isFavorite());
    }


    @Test
    void testDeleteContact() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("Mark");
        contact.setLastName("Taylor");
        contact.setPhoneNumber("333333333");
        contact.setEmail("mark@example.com");
        contact.setFavorite(false);
        Contact saved = contactRepository.save(contact);

        mockMvc.perform(post("/contacts/delete/" + saved.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));

        Optional<Contact> deleted = contactRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }
}
