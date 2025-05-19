package com.capsule.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Capsule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private LocalDate unlockDate;

    private String username; // Link to user via username

    // Getters and setters
    // (Generate with IDE or Lombok if you're using it)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDate getUnlockDate() { return unlockDate; }
    public void setUnlockDate(LocalDate unlockDate) { this.unlockDate = unlockDate; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
