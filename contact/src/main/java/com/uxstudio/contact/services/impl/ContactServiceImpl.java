package com.uxstudio.contact.services.impl;

import com.uxstudio.contact.domains.Contact;
import com.uxstudio.contact.domains.ContactEntity;
import com.uxstudio.contact.repositories.ContactRepository;
import com.uxstudio.contact.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public Contact saveContact(final Contact contact) {
        final ContactEntity contactEntity = contactToContactEntity(contact);
        final ContactEntity savedContactEntity = contactRepository.save(contactEntity);
        return contactEntityToContact(savedContactEntity);
    }

    @Override
    public String saveProfilePicture(MultipartFile profilePicture) {
        try {
            String uploadDir = "uploads";

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "-" + profilePicture.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store profile picture", e);
        }
    }


    @Override
    public Optional<Contact> getContactById(Long id) {
        var foundContact = contactRepository.findById(id);
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
    public void deleteContactById(Long id) {
        try {
            contactRepository.deleteById(id);

        } catch (final EmptyResultDataAccessException ex) {
            log.debug("Attempted to delete non-existing contact", ex);
        }
    }

    private ContactEntity contactToContactEntity(Contact contact) {
        return ContactEntity.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phoneNumber(contact.getPhoneNumber())
                .email(contact.getEmail())
                .profilePictureUrl(contact.getProfilePictureUrl()) // Include profilePictureUrl
                .build();
    }

    private Contact contactEntityToContact(ContactEntity contactEntity) {
        return Contact.builder()
                .id(contactEntity.getId())
                .name(contactEntity.getName())
                .phoneNumber(contactEntity.getPhoneNumber())
                .email(contactEntity.getEmail())
                .profilePictureUrl(contactEntity.getProfilePictureUrl()) // Include profilePictureUrl
                .build();
    }

}
