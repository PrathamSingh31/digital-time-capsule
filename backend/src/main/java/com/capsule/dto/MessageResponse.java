package com.capsule.dto;

import java.time.LocalDate;

public class MessageResponse {
    private String message;
    private LocalDate unlockDate;

    public MessageResponse(String message, LocalDate unlockDate) {
        this.message = message;
        this.unlockDate = unlockDate;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getUnlockDate() {
        return unlockDate;
    }
}
