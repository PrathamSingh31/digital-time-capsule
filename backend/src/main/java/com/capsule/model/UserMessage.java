package com.capsule.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String message;

    private LocalDate unlockDate;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getUnlockDate() {
        return unlockDate;
    }

    public void setUnlockDate(LocalDate unlockDate) {
        this.unlockDate = unlockDate;
    }
}
