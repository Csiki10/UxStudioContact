package com.uxstudio.contact.controllers;

import com.uxstudio.contact.domains.Contact;
import com.uxstudio.contact.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ContactController {
    private ContactService contactService;

    @Autowired
    public ContactController(final ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping(path = "/contact/{id}") // put
    public ResponseEntity<Contact> createContact(
            @PathVariable final int id,
            @RequestBody final Contact contact) {
        contact.setId(id);
        final Contact savedContact = contactService.create(contact);
        return new ResponseEntity<Contact>(savedContact, HttpStatus.CREATED);
    }

    @GetMapping(path = "/contact/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable final int id) {
        final Optional<Contact> foundContact = contactService.getContactById(id);
        return foundContact
            .map(contact -> new ResponseEntity<Contact>(contact, HttpStatus.OK))
            .orElse(new ResponseEntity<Contact>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/contact")
    public ResponseEntity<List<Contact>> getAllContacts() {
        return new ResponseEntity<List<Contact>>(contactService.getAllContacts(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/contact/{id}")
    public ResponseEntity deleteContact(@PathVariable final int id) {
        contactService.deleteContactById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // todo PUT
}
