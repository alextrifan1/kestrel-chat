package com.kestrel.chat.user;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.UUID;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String email;
    @Column(nullable = false, length = 32)
    private String username;
    @Column(nullable = false, length = 64)
    private String displayName;
    @Column(nullable = false, length = 255)
    private String passwordHash;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
    @Column(nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    protected User() {}

    public User(String email, String username, String displayName, String passwordHash) {
        this.email = email;
        this.username = username;
        this.displayName = displayName;
        this.passwordHash = passwordHash;
        //this.createdAt = Instant.now(); hibernate would overwrite this
    }

    public UUID getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
