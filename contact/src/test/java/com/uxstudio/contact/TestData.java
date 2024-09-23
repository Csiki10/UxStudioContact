package com.uxstudio.contact;

import com.uxstudio.contact.domains.Contact;
import com.uxstudio.contact.domains.ContactEntity;

public final class TestData {

    private TestData() {}

    // Adding default profile picture URL
    private static final String DEFAULT_PROFILE_PICTURE_URL = "uploads/default-profile-picture.jpg";

    public static Contact testContact() {
        return Contact.builder()
                .id(1L)
                .name("Anna")
                .phoneNumber("+36706687666")
                .email("csb@gmail.com")
                .profilePictureUrl(DEFAULT_PROFILE_PICTURE_URL) // Adding profile picture URL
                .build();
    }

    public static ContactEntity testContactEntity() {
        return ContactEntity.builder()
                .id(1L)
                .name("Anna")
                .phoneNumber("+36706687666")
                .email("csb@gmail.com")
                .profilePictureUrl(DEFAULT_PROFILE_PICTURE_URL) // Adding profile picture URL
                .build();
    }
}
