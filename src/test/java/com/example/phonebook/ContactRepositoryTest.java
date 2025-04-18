package com.example.phonebook;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.data.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // This only loads JPA-related components (i.e., repository and database configurations)
@Transactional  // Ensures changes are rolled back after each test, keeping the database clean
public class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    private Contact contact;

    @BeforeEach
    void setUp() {
        // Initialize the Contact object to be used in the tests
        contact = new Contact(1L, "John", "Doe", "+1234567890", "john.doe@example.com", false);
    }

    @Test
    public void testCreateContact() {
        // Save the contact
        Contact savedContact = contactRepository.save(contact);
        assertNotNull(savedContact);
        assertEquals(contact.getFirstName(), savedContact.getFirstName());
        assertEquals(contact.getLastName(), savedContact.getLastName());
        assertEquals(contact.getPhoneNumber(), savedContact.getPhoneNumber());
        assertEquals(contact.getEmail(), savedContact.getEmail());
        assertEquals(contact.isFavorite(), savedContact.isFavorite());
    }

    @Test
    public void testReadContact() {
        // Save and retrieve a contact
        contactRepository.save(contact);
        Contact foundContact = contactRepository.findById(contact.getId()).orElse(null);
        assertNotNull(foundContact);
        assertEquals(contact.getFirstName(), foundContact.getFirstName());
        assertEquals(contact.getLastName(), foundContact.getLastName());
        assertEquals(contact.getPhoneNumber(), foundContact.getPhoneNumber());
        assertEquals(contact.getEmail(), foundContact.getEmail());
        assertEquals(contact.isFavorite(), foundContact.isFavorite());
    }

    @Test
    public void testUpdateContact() {
        // Save a contact, then update it
        contactRepository.save(contact);

        contact.setFirstName("Jane");
        contact.setLastName("Smith");
        contact.setEmail("jane@example.com");
        contact.setPhoneNumber("+9876543210");
        contact.setFavorite(true);

        Contact updatedContact = contactRepository.save(contact);

        assertNotNull(updatedContact);
        assertEquals("Jane", updatedContact.getFirstName());
        assertEquals("Smith", updatedContact.getLastName());
        assertEquals("jane@example.com", updatedContact.getEmail());
        assertEquals("+9876543210", updatedContact.getPhoneNumber());
        assertTrue(updatedContact.isFavorite());
    }

    @Test
    public void testDeleteContact() {
        // Save and then delete a contact
        contactRepository.save(contact);
        contactRepository.delete(contact);

        Contact deletedContact = contactRepository.findById(contact.getId()).orElse(null);
        assertNull(deletedContact, "Contact should be null after deletion");
    }
}