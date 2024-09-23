package com.uxstudio.contact.controllers;

import com.uxstudio.contact.domains.Contact;
import com.uxstudio.contact.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(final ContactService service) {
        this.contactService = service;
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(
            @RequestPart("contact") Contact contact,
            @RequestPart("profilePicture") MultipartFile profilePicture) {
        String profilePictureUrl = contactService.saveProfilePicture(profilePicture);
        contact.setProfilePictureUrl(profilePictureUrl);
        Contact savedContact = contactService.saveContact(contact);

        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(
            @PathVariable Long id,
            @RequestPart("contact") Contact contact,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        contact.setId(id);

        if (profilePicture != null) {
            String profilePictureUrl = contactService.saveProfilePicture(profilePicture);
            contact.setProfilePictureUrl(profilePictureUrl);
        }

        Contact updatedContact = contactService.saveContact(contact);
        return new ResponseEntity<>(updatedContact, HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable final Long id) {
        final Optional<Contact> foundContact = contactService.getContactById(id);
        return foundContact
            .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Contact>> getAllContacts() {
        return new ResponseEntity<>(contactService.getAllContacts(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteContact(@PathVariable final Long id) {
        contactService.deleteContactById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
