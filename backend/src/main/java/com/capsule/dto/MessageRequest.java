package com.capsule.dto;

import java.time.LocalDate;

public class MessageRequest {
    private String message;
    private LocalDate unlockDate;

    // Getters and setters
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
