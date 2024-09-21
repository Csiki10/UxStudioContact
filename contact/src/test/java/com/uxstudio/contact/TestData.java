package com.uxstudio.contact;

import com.uxstudio.contact.domains.Contact;
import com.uxstudio.contact.domains.ContactEntity;

public final class TestData {

    private TestData() {}

    public static Contact testContact() {
        return Contact.builder()
            .id(123)
            .name("Anna")
            .phoneNumber("+36706687666")
            .email("csb@gmail.com")
            .build();
    }

    public static ContactEntity testContactEntity() {
        return ContactEntity.builder()
            .id(123)
            .name("Anna")
            .phoneNumber("+36706687666")
            .email("csb@gmail.com")
            .build();
    }
}
