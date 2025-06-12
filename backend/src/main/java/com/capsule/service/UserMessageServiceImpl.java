package com.capsule.service;

import com.capsule.dto.MessageRequest;
import com.capsule.model.User;
import com.capsule.model.UserMessage;
import com.capsule.repository.UserMessageRepository;
import com.capsule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserMessage createMessage(Long userId, MessageRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        UserMessage userMessage = new UserMessage();
        userMessage.setTitle(request.getTitle());
        userMessage.setContent(request.getContent());

        String deliveryDateStr = request.getDeliveryDate();
        if (deliveryDateStr != null && !deliveryDateStr.isEmpty()) {
            LocalDate deliveryDate = LocalDate.parse(deliveryDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            userMessage.setMessageDateTime(deliveryDate.atStartOfDay());
            userMessage.setYear(deliveryDate.getYear());
        } else {
            LocalDateTime now = LocalDateTime.now();
            userMessage.setMessageDateTime(now);
            userMessage.setYear(now.getYear());
        }

        userMessage.setUser(user);
        return userMessageRepository.save(userMessage);
    }

    @Override
    public List<UserMessage> getMessagesByUserId(Long userId) {
        return userMessageRepository.findByUserId(userId);
    }

    @Override
    public UserMessage updateMessage(Long messageId, Long userId, MessageRequest request) {
        UserMessage existingMessage = userMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));

        if (!existingMessage.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to update message.");
        }

        existingMessage.setTitle(request.getTitle());
        existingMessage.setContent(request.getContent());

        String deliveryDateStr = request.getDeliveryDate();
        if (deliveryDateStr != null && !deliveryDateStr.isEmpty()) {
            LocalDate deliveryDate = LocalDate.parse(deliveryDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            existingMessage.setMessageDateTime(deliveryDate.atStartOfDay());
            existingMessage.setYear(deliveryDate.getYear());
        }

        return userMessageRepository.save(existingMessage);
    }

    @Override
    public void deleteMessage(Long messageId, Long userId) {
        UserMessage existingMessage = userMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));

        if (!existingMessage.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to delete message.");
        }

        userMessageRepository.delete(existingMessage);
    }

    @Override
    public List<UserMessage> getMessagesByUserUsername(String username) {
        return userMessageRepository.findByUserUsername(username);
    }

    /**
     * Imports a list of messages (JSON array from frontend) for a given user.
     */
    @Override
    public void importMessages(Long userId, List<MessageRequest> messages) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        for (MessageRequest request : messages) {
            UserMessage message = new UserMessage();
            message.setTitle(request.getTitle());
            message.setContent(request.getContent());

            String deliveryDateStr = request.getDeliveryDate();
            if (deliveryDateStr != null && !deliveryDateStr.isEmpty()) {
                LocalDate deliveryDate = LocalDate.parse(deliveryDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                message.setMessageDateTime(deliveryDate.atStartOfDay());
                message.setYear(deliveryDate.getYear());
            } else {
                LocalDateTime now = LocalDateTime.now();
                message.setMessageDateTime(now);
                message.setYear(now.getYear());
            }

            message.setUser(user);
            userMessageRepository.save(message);
        }
    }

    /**
     * File import not supported yet.
     */
    @Override
    public void importMessages(Long userId, org.springframework.web.multipart.MultipartFile file, String fileType) {
        throw new UnsupportedOperationException("File-based import not implemented yet.");
    }
}
