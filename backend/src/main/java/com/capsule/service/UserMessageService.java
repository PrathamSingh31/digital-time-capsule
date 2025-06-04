package com.capsule.service;

import com.capsule.model.UserMessage;
import com.capsule.dto.MessageRequest;

import java.util.List;

public interface UserMessageService {
    UserMessage createMessage(Long userId, MessageRequest request);
    List<UserMessage> getMessagesByUserId(Long userId);
}
