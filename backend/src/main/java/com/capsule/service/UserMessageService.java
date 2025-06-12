package com.capsule.service;

import com.capsule.dto.MessageRequest;
import com.capsule.model.UserMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserMessageService {

    UserMessage createMessage(Long userId, MessageRequest request);

    List<UserMessage> getMessagesByUserId(Long userId);

    UserMessage updateMessage(Long messageId, Long userId, MessageRequest request);

    void deleteMessage(Long messageId, Long userId);

    // ✅ Existing method: for file-based upload
    void importMessages(Long userId, MultipartFile file, String fileType) throws Exception;

    // ✅ New method: for JSON array upload
    void importMessages(Long userId, List<MessageRequest> messages);

    List<UserMessage> getMessagesByUserUsername(String username);
}
