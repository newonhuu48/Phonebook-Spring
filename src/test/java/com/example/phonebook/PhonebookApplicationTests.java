package com.example.phonebook;

import com.example.phonebook.data.entity.Contact;
import com.example.phonebook.data.repository.ContactRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class PhonebookApplicationTests {

	@Autowired
	private ContactRepository contactRepository;

	private Contact contact;


	@BeforeEach
	public void setUp() {
		//Setup test contact before each test
		contact = new Contact(1L, "John", "Doe", "1234567890", "john.doe@example.com", false);
	}

	@Test
	public void testCreateContact() {
		// Create and save a new contact
		Contact savedContact = contactRepository.save(contact);

		// Check if the contact is saved properly
		assertNotNull(savedContact.getId());
		assertEquals("John", savedContact.getFirstName());
		assertEquals("Doe", savedContact.getLastName());
		assertEquals("john.doe@example.com", savedContact.getEmail());
	}

	@Test
	public void testReadContact() {
		// Save the contact first
		contactRepository.save(contact);

		// Retrieve the contact by id
		Contact fetchedContact = contactRepository.findById(contact.getId()).orElse(null);

		// Check if the contact was found and matches the expected details
		assertNotNull(fetchedContact);
		assertEquals(contact.getFirstName(), fetchedContact.getFirstName());
		assertEquals(contact.getLastName(), fetchedContact.getLastName());
	}

	@Test
	public void testUpdateContact() {
		// Save the contact first
		Contact savedContact = contactRepository.save(contact);

		// Update the contact
		savedContact.setEmail("new.email@example.com");
		Contact updatedContact = contactRepository.save(savedContact);

		// Check if the contact's email was updated
		assertEquals("new.email@example.com", updatedContact.getEmail());
	}


}
