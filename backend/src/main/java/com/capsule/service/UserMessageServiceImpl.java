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
import java.util.*;

import java.util.stream.Collectors;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private UserRepository userRepository;

    private final FileStorageService fileStorageService;

    @Autowired
    public UserMessageServiceImpl(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

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

        String unlockDateStr = request.getUnlockDate();
        if (unlockDateStr != null && !unlockDateStr.isEmpty()) {
            LocalDate unlockDate = LocalDate.parse(unlockDateStr);
            userMessage.setUnlockDate(unlockDate.atStartOfDay());
        }

        userMessage.setUser(user);
        return userMessageRepository.save(userMessage);
    }

    @Override
    public UserMessage createMessageWithImage(Long userId,
                                              String title,
                                              String content,
                                              String deliveryDate,
                                              String unlockDate,
                                              MultipartFile imageFile) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserMessage message = new UserMessage();
        message.setTitle(title);
        message.setContent(content);

        // Set deliveryDate
        if (deliveryDate != null && !deliveryDate.isEmpty()) {
            LocalDate date = LocalDate.parse(deliveryDate, DateTimeFormatter.ISO_LOCAL_DATE);
            message.setMessageDateTime(date.atStartOfDay());
            message.setYear(date.getYear());
        } else {
            LocalDateTime now = LocalDateTime.now();
            message.setMessageDateTime(now);
            message.setYear(now.getYear());
        }

        // Set unlockDate
        if (unlockDate != null && !unlockDate.isEmpty()) {
            LocalDate unlock = LocalDate.parse(unlockDate, DateTimeFormatter.ISO_LOCAL_DATE);
            message.setUnlockDate(unlock.atStartOfDay());
        }

        // Save image
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveImage(imageFile);
            message.setImageUrl(imagePath);
        }

        message.setUser(user);
        return userMessageRepository.save(message);
    }


    @Override
    public UserMessage createMessageWithMedia(Long userId,
                                              String title,
                                              String content,
                                              String deliveryDate,
                                              String unlockDate,
                                              MultipartFile imageFile,
                                              MultipartFile videoFile) {
        UserMessage message = new UserMessage();
        message.setTitle(title);
        message.setContent(content);

        if (deliveryDate != null && !deliveryDate.isEmpty()) {
            LocalDate delivery = LocalDate.parse(deliveryDate, DateTimeFormatter.ISO_LOCAL_DATE);
            message.setMessageDateTime(delivery.atStartOfDay());
            message.setYear(delivery.getYear());
        }

        if (unlockDate != null && !unlockDate.isEmpty()) {
            LocalDate unlock = LocalDate.parse(unlockDate, DateTimeFormatter.ISO_LOCAL_DATE);
            message.setUnlockDate(unlock.atStartOfDay());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveImage(imageFile);
            message.setImageUrl(imagePath);
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoPath = fileStorageService.saveVideo(videoFile);
            message.setVideoUrl(videoPath);
        }

        message.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));

        return userMessageRepository.save(message);
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

        // ✅ Block editing if unlockDate is in the future
        if (existingMessage.getUnlockDate() != null && existingMessage.getUnlockDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Message is locked until: " + existingMessage.getUnlockDate());
        }

        existingMessage.setTitle(request.getTitle());
        existingMessage.setContent(request.getContent());

        String deliveryDateStr = request.getDeliveryDate();
        if (deliveryDateStr != null && !deliveryDateStr.isEmpty()) {
            LocalDate deliveryDate = LocalDate.parse(deliveryDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            existingMessage.setMessageDateTime(deliveryDate.atStartOfDay());
            existingMessage.setYear(deliveryDate.getYear());
        }

        String unlockDateStr = request.getUnlockDate();
        if (unlockDateStr != null && !unlockDateStr.isEmpty()) {
            LocalDate unlockDate = LocalDate.parse(unlockDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            existingMessage.setUnlockDate(unlockDate.atStartOfDay());
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

        if (existingMessage.getUnlockDate() != null && existingMessage.getUnlockDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Cannot delete. Message is locked until: " + existingMessage.getUnlockDate());
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
    public UserMessage getMessageByIdAndUsername(Long messageId, String username) {
        UserMessage message = userMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to view this message");
        }

        return message;
    }

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

    @Override
    public UserMessage getMessageByShareToken(String token) {
        return userMessageRepository.findByShareToken(token)
                .orElseThrow(() -> new RuntimeException("Message not found or link invalid"));
    }

    @Override
    public String enableSharing(Long messageId, Long userId) {
        UserMessage message = userMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        String token = UUID.randomUUID().toString();
        message.setShareToken(token);
        userMessageRepository.save(message);
        return token;
    }

    @Override
    public void disableSharing(Long messageId, Long userId) {
        UserMessage message = userMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        message.setShareToken(null);
        userMessageRepository.save(message);
    }

    @Override
    public UserMessage updateMessageWithMedia(Long messageId, Long userId, String title, String content,
                                              String deliveryDate, String unlockDate,
                                              MultipartFile imageFile, MultipartFile videoFile) {

        UserMessage userMessage = userMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!userMessage.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this message");
        }

        // ✅ Block editing if unlockDate is in the future
        if (userMessage.getUnlockDate() != null && userMessage.getUnlockDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("This message is locked until: " + userMessage.getUnlockDate());
        }

        userMessage.setTitle(title);
        userMessage.setContent(content);

        if (deliveryDate != null && !deliveryDate.isEmpty()) {
            LocalDate date = LocalDate.parse(deliveryDate, DateTimeFormatter.ISO_LOCAL_DATE);
            userMessage.setMessageDateTime(date.atStartOfDay());
            userMessage.setYear(date.getYear());
        }

        // ✅ Parse and set unlockDate (yyyy-MM-dd)
        if (unlockDate != null && !unlockDate.isEmpty()) {
            LocalDate unlock = LocalDate.parse(unlockDate, DateTimeFormatter.ISO_LOCAL_DATE);
            userMessage.setUnlockDate(unlock.atStartOfDay());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveImage(imageFile);
            userMessage.setImageUrl(imagePath);
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoPath = fileStorageService.saveVideo(videoFile);
            userMessage.setVideoUrl(videoPath);
        }

        return userMessageRepository.save(userMessage);
    }



}
