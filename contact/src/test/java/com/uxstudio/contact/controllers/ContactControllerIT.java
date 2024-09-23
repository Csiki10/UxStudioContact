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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.uxstudio.contact.TestData.testContact;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ContactControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactService contactService;

    @Test
    public void testThatContactIsCreatedWithProfilePicture() throws Exception {
        final Contact contact = testContact();

        // Create a MockMultipartFile for the profile picture
        MockMultipartFile mockFile = new MockMultipartFile("profilePicture", "picture.jpg", "image/jpeg", "fake-image-content".getBytes());

        // Convert the contact to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String contactJson = objectMapper.writeValueAsString(contact);
        MockMultipartFile contactFile = new MockMultipartFile("contact", "", "application/json", contactJson.getBytes());

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/contacts")
            .file(mockFile) // Attach the profile picture
            .file(contactFile) // Attach the contact JSON
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contact.getId()))

        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(contact.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contact.getPhoneNumber()));
    }

    @Test
    public void testThatRetrieveContactReturns404WhenContactNotFound() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/contacts/123123123"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatRetrieveContactReturnsHttp200AndContactWhenExists() throws Exception {
        final Contact contact = testContact();
        contactService.saveContact(contact);

        mockMvc
            .perform(MockMvcRequestBuilders.get("/contacts/" + contact.getId())) // Ensure the correct URL
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contact.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contact.getPhoneNumber()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(contact.getEmail()));
    }

    @Test
    public void testThatGetAllContactsReturnsHttp200EmptyListWhenNoContactsExist() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/contacts"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

//    @Test
//    public void testThatGetAllContactsReturnsHttp200AndContactsWhenContactsExist() throws Exception {
//        final Contact contact = testContact();
//        contactService.saveContact(contact);
//
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/contacts"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contact.getId()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact.getName()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contact.getPhoneNumber()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(contact.getEmail()));
//    }

    @Test
    public void testThatHttp204IsReturnedWhenExistingContactIsDeleted() throws Exception {
        final Contact contact = testContact();
        contactService.saveContact(contact);

        mockMvc
            .perform(MockMvcRequestBuilders.delete("/contacts/" + contact.getId()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
