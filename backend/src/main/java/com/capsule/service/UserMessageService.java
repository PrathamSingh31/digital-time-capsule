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
    void importMessages(Long userId, MultipartFile file, String fileType) throws IOException;
    void importMessages(Long userId, List<MessageRequest> messages);
    List<UserMessage> getFilteredMessages(Long userId, Integer year, String sort);
    List<UserMessage> getScheduledMessages(Long userId);
    UserMessage getMessageByShareToken(String token);          // ✅ NEW
    String enableSharing(Long messageId, Long userId);         // ✅ NEW
    void disableSharing(Long messageId, Long userId);          // ✅ NEW
    UserMessage createMessageWithImage(Long userId, String title, String content, String deliveryDate, MultipartFile imageFile);
    UserMessage createMessageWithMedia(Long userId, String title, String content, String deliveryDate, MultipartFile imageFile, MultipartFile videoFile);
    UserMessage updateMessageWithMedia(Long messageId, Long userId, String title, String content, String deliveryDate, MultipartFile image, MultipartFile video);


}
