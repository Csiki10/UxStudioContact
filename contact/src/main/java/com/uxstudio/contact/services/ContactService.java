package com.uxstudio.contact.services;

import com.uxstudio.contact.domains.Contact;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    Contact saveContact(Contact contact);
    Optional<Contact> getContactById(Long id);
    List<Contact> getAllContacts();
    void deleteContactById(Long id);
    String saveProfilePicture(MultipartFile profilePicture);
}
