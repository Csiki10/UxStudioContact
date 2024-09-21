package com.uxstudio.contact.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxstudio.contact.domains.Contact;
import com.uxstudio.contact.services.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.uxstudio.contact.TestData.testContact;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ContactControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactService contactService;

    @Test
    public void TestThatContactIsCreated() throws Exception {
        final Contact contact = testContact();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String contactJson = objectMapper.writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders.post("/contact/"+contact.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(contactJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contact.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(contact.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contact.getPhoneNumber()));
    }

    @Test
    public void testThatRetrieveContactReturns404WhenContactNotFound() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/contact/123123123"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatRetrieveContactReturnsHttp200AndContactWhenExists() throws Exception {
        final Contact contact = testContact();
        contactService.create(contact);

        mockMvc
            .perform(MockMvcRequestBuilders.get("/contact/" + contact.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contact.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contact.getPhoneNumber()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(contact.getEmail()));
    }

    @Test
    public void testThatGetAllContactsReturnsHttp200EmptyListWhenNoContactsExist() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/contact"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testThatGetAllContactsReturnsHttp200AndContactsWhenContactsExist() throws Exception {
        final Contact contact = testContact();
        contactService.create(contact);

        mockMvc
            .perform(MockMvcRequestBuilders.get("/contact"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contact.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contact.getPhoneNumber()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(contact.getEmail()));
    }

    @Test
    public void testThatHttp204IsReturnedWhenExistingContactIsDeleted() throws Exception {
        final Contact contact = testContact();
        contactService.create(contact);

        mockMvc
            .perform(MockMvcRequestBuilders.delete("/contact/" + contact.getId()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
