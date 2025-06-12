package com.capsule.service;

import com.capsule.dto.MessageRequest;
import com.capsule.model.UserMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserMessageService {
    UserMessage createMessage(Long userId, MessageRequest request);
    List<UserMessage> getMessagesByUserId(Long userId);
    UserMessage updateMessage(Long messageId, Long userId, MessageRequest request);
    void deleteMessage(Long messageId, Long userId);
    List<UserMessage> getMessagesByUserUsername(String username);

    // ✅ Add these two methods if using both import types
    void importMessages(Long userId, List<MessageRequest> messages);

    void importMessages(Long userId, MultipartFile file, String fileType) throws IOException;
}
