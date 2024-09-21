package com.uxstudio.contact.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "contacts")
public class ContactEntity {
    @Id
    private int id;
    private String name;
    private String phoneNumber;
    private String email;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
