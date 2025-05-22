package com.example.phonebook;

import com.example.phonebook.dto.ContactDTO;
import com.example.phonebook.service.ContactService;
import com.example.phonebook.web.controller.ContactController;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private ModelMapper modelMapper;


    //READ MVC TEST
    @Test
    void shouldReturnContactsListPage() throws Exception {
        Page<ContactDTO> emptyPage = new PageImpl<>(List.of());
        when(contactService.getContactsByUser(any(), any(), any(), any(), anyInt(), anyInt(), any(), any()))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeExists("contactsPage"));
    }


    //CREATE MVC TEST
    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/contacts/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeExists("contact"));
    }

    @Test
    void shouldSubmitCreateFormWithoutErrors() throws Exception {
        mockMvc.perform(post("/contacts/create")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("phoneNumber", "+1234567890")
                        .param("email", "john.doe@example.com")
                        .param("favorite", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));
    }


    //UPDATE MVC TEST
    @Test
    void shouldShowEditForm() throws Exception {
        long contactId = 1L;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contactId);
        contactDTO.setFirstName("John");
        contactDTO.setLastName("Doe");
        contactDTO.setPhoneNumber("+1234567890");
        contactDTO.setEmail("john.doe@example.com");
        contactDTO.setFavorite(false);

        // Mock the service call
        when(contactService.getContactById(contactId)).thenReturn(contactDTO);

        mockMvc.perform(get("/contacts/edit/{id}", contactId))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("contact"));
    }


    @Test
    void shouldSubmitEditFormWithoutErrors() throws Exception {
        long contactId = 1L;

        mockMvc.perform(post("/contacts/edit/{id}", contactId)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("phoneNumber", "+1234567890")
                        .param("email", "john.doe@example.com")
                        .param("favorite", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));
    }


    //DELETE MVC TEST
    @Test
    void shouldShowDeleteForm() throws Exception {
        long contactId = 1L;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contactId);
        contactDTO.setFirstName("John");
        contactDTO.setLastName("Doe");
        contactDTO.setPhoneNumber("+1234567890");
        contactDTO.setEmail("john.doe@example.com");
        contactDTO.setFavorite(false);

        when(contactService.getContactById(contactId)).thenReturn(contactDTO);

        mockMvc.perform(get("/contacts/delete/{id}", contactId))
                .andExpect(status().isOk())
                .andExpect(view().name("delete"))
                .andExpect(model().attributeExists("contact"));
    }

    @Test
    void shouldSubmitDeleteForm() throws Exception {
        long contactId = 1L;

        mockMvc.perform(post("/contacts/delete/{id}", contactId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));
    }
}
