package com.toolborrow.backend.model.entity;

import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    // hashed password
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 10)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 255)
    private String streetAddress;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Tool> tools;

    @OneToMany(mappedBy = "renter")
    private List<Conversation> renterConversations;

    @OneToMany(mappedBy = "sentBy")
    private List<Message> messagesSent;
}
