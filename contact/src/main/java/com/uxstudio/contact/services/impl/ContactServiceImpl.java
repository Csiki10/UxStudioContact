package com.uxstudio.contact.services.impl;

import com.uxstudio.contact.domains.Contact;
import com.uxstudio.contact.domains.ContactEntity;
import com.uxstudio.contact.repositories.ContactRepository;
import com.uxstudio.contact.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(final ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact create(final Contact contact) {
        final ContactEntity contactEntity = contactToContactEntity(contact);
        final ContactEntity savedContactEntity = contactRepository.save(contactEntity);
        return contactEntityToContact(savedContactEntity);
    }

    @Override
    public Optional<Contact> getContactById(int id) {
        var foundContact = contactRepository.findById((long)id);
        return foundContact.map(contact -> contactEntityToContact(contact));
    }

    @Override
    public List<Contact> getAllContacts() {
        var foundContacts = contactRepository.findAll();
        return foundContacts
                .stream()
                .map(contact -> contactEntityToContact(contact))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteContactById(int id) {
        try {
            contactRepository.deleteById((long)id);

        } catch (final EmptyResultDataAccessException ex) {
            log.debug("Attempted to delete non-existing contact", ex);
        }
    }

    // todo PUT

    private ContactEntity contactToContactEntity(Contact contact) {
        return ContactEntity.builder()
            .id(contact.getId())
            .name(contact.getName())
            .phoneNumber(contact.getPhoneNumber())
            .email(contact.getEmail())
            .build();
    }

    private Contact contactEntityToContact(ContactEntity contactEntity) {
        return Contact.builder()
            .id(contactEntity.getId())
            .name(contactEntity.getName())
            .phoneNumber(contactEntity.getPhoneNumber())
            .email(contactEntity.getEmail())
            .build();
    }
}
