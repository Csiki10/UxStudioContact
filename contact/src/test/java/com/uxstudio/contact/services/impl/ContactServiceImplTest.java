package com.uxstudio.contact.services.impl;

import com.uxstudio.contact.domains.Contact;
import com.uxstudio.contact.domains.ContactEntity;
import com.uxstudio.contact.repositories.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.uxstudio.contact.TestData.testContact;
import static com.uxstudio.contact.TestData.testContactEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactServiceImpl underTest;

    @Test
    public void testThatContactIsSavedWithProfilePicture() throws Exception {
        final Contact contact = testContact();
        final ContactEntity contactEntity = testContactEntity();

        when(contactRepository.save(eq(contactEntity))).thenReturn(contactEntity);

        final Contact savedContact = underTest.saveContact(contact);
        assertEquals(contact, savedContact);
    }

    @Test
    public void testThatGetContactByIdReturnsEmptyWhenNoContact() {
        final long id = 123L;
        when(contactRepository.findById(eq(id))).thenReturn(Optional.empty());

        final Optional<Contact> result = underTest.getContactById(id);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testThatGetContactByIdReturnsContactWhenExists() {
        final Contact contact = testContact();
        final ContactEntity contactEntity = testContactEntity();

        when(contactRepository.findById(eq((long) contact.getId()))).thenReturn(Optional.of(contactEntity)); // cast to long

        final Optional<Contact> result = underTest.getContactById(contact.getId());
        assertEquals(Optional.of(contact), result);
    }

    @Test
    public void testGetAllContactsReturnsEmptyListWhenNoContactsExist() {
        when(contactRepository.findAll()).thenReturn(new ArrayList<ContactEntity>());
        final List<Contact> result = underTest.getAllContacts();
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllContactsReturnsContactsWhenExist() {
        final ContactEntity contactEntity = testContactEntity();
        when(contactRepository.findAll()).thenReturn(List.of(contactEntity));
        final List<Contact> result = underTest.getAllContacts();
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteContactDeletesContact() {
        final long id = 123L;
        underTest.deleteContactById(id);
        verify(contactRepository, times(1)).deleteById(eq(id));
    }
}
