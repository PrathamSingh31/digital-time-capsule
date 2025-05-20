package com.capsule.service;

import com.capsule.model.UserMessage;

import java.time.LocalDate;
import java.util.List;

public interface UserMessageService {
    void saveMessage(String username, String message, LocalDate unlockDate);
    List<UserMessage> getMessagesByUsername(String username); // <-- Add this
}
