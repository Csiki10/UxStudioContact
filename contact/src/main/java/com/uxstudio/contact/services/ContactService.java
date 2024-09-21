package com.uxstudio.contact.services;

import com.uxstudio.contact.domains.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    Contact create(Contact contact);
    Optional<Contact> getContactById(int id);
    List<Contact> getAllContacts();
    void deleteContactById(int id);

    // todo PUT
}
