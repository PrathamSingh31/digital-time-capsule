package com.capsule.service;

import com.capsule.dto.MessageRequest;
import com.capsule.model.UserMessage;

import java.util.List;

public interface UserMessageService {

    UserMessage createMessage(Long userId, MessageRequest request);

    List<UserMessage> getMessagesByUser(Long userId, Integer year, String sort);
}
