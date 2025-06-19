package com.capsule.service;

import com.capsule.dto.MessageRequest;
import com.capsule.model.User;
import com.capsule.model.UserMessage;
import com.capsule.repository.UserMessageRepository;
import com.capsule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserMessage createMessage(Long userId, MessageRequest request) {
        System.out.println("✅ Creating message for userId: " + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ User not found in DB with ID: " + userId));

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
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!existingMessage.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
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
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!existingMessage.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        userMessageRepository.delete(existingMessage);
    }

    @Override
    public List<UserMessage> getMessagesByUserUsername(String username) {
        return userMessageRepository.findByUserUsername(username);
    }

    @Override
    public void importMessages(Long userId, MultipartFile file, String fileType) throws IOException {
        throw new UnsupportedOperationException("File-based import not implemented yet.");
    }

    @Override
    public void importMessages(Long userId, List<MessageRequest> messages) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ User not found with ID: " + userId));

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

    @Override
    public List<UserMessage> getFilteredMessages(Long userId, Integer year, String sort) {
        List<UserMessage> messages = userMessageRepository.findByUserId(userId);

        if (year != null) {
            messages = messages.stream()
                    .filter(msg -> msg.getYear() == year)
                    .collect(Collectors.toList());
        }

        if ("asc".equalsIgnoreCase(sort)) {
            messages.sort(Comparator.comparing(UserMessage::getMessageDateTime));
        } else if ("desc".equalsIgnoreCase(sort)) {
            messages.sort(Comparator.comparing(UserMessage::getMessageDateTime).reversed());
        }

        return messages;
    }

    @Override
    public List<UserMessage> getScheduledMessages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime now = LocalDateTime.now();
        return userMessageRepository.findByUserAndMessageDateTimeAfter(user, now);
    }
}
