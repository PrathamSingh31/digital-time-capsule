package com.capsule.service;

import com.capsule.model.UserMessage;

import java.util.List;

public interface UserMessageService {
    void saveMessage(UserMessage message);
    List<UserMessage> getMessagesByUsername(String username);
}
