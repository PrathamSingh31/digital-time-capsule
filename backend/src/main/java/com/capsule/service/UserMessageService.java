package com.capsule.service;

import com.capsule.dto.MessageRequest;
import com.capsule.model.UserMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserMessageService {

    // Create a new message for the user
    UserMessage createMessage(Long userId, MessageRequest request);

    // Retrieve all messages for a specific user by userId
    List<UserMessage> getMessagesByUserId(Long userId);

    // Update an existing message for a user
    UserMessage updateMessage(Long messageId, Long userId, MessageRequest request);

    // Delete a message for a user
    void deleteMessage(Long messageId, Long userId);

    // Retrieve messages by username
    List<UserMessage> getMessagesByUserUsername(String username);

    // Import messages from a file (CSV/JSON/etc.)
    void importMessages(Long userId, MultipartFile file, String fileType) throws IOException;

    // Import messages from a list (used for frontend/JSON bulk import)
    void importMessages(Long userId, List<MessageRequest> messages);

    // Filter messages by optional year and sort (asc/desc)
    List<UserMessage> getFilteredMessages(Long userId, Integer year, String sort);

    // Get messages scheduled for future delivery
    List<UserMessage> getScheduledMessages(Long userId);
}
