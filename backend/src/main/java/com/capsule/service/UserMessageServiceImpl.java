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
import java.util.List;
import java.util.Optional;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    private final UserMessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserMessageServiceImpl(UserMessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserMessage createMessage(Long userId, MessageRequest request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOpt.get();
        UserMessage message = new UserMessage();
        message.setUser(user);

        // Ensure required fields are set to avoid not-null errors
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new RuntimeException("Content must not be null or empty.");
        }

        message.setContent(request.getContent());
        message.setTitle(request.getTitle());

        if (request.getDeliveryDate() != null && !request.getDeliveryDate().isEmpty()) {
            message.setDeliveryDate(LocalDate.parse(request.getDeliveryDate()));
        }

        message.setCreatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public List<UserMessage> getMessagesByUser(Long userId, Integer year, String sort) {
        List<UserMessage> messages;

        if (year != null && sort != null) {
            if (sort.equalsIgnoreCase("asc")) {
                messages = messageRepository.findByUserIdAndYearOrderByDeliveryDateAsc(userId, year);
            } else {
                messages = messageRepository.findByUserIdAndYearOrderByDeliveryDateDesc(userId, year);
            }
        } else if (year != null) {
            messages = messageRepository.findByUserIdAndYear(userId, year);
        } else if (sort != null) {
            if (sort.equalsIgnoreCase("asc")) {
                messages = messageRepository.findByUserIdOrderByDeliveryDateAsc(userId);
            } else {
                messages = messageRepository.findByUserIdOrderByDeliveryDateDesc(userId);
            }
        } else {
            messages = messageRepository.findByUserId(userId);
        }

        return messages;
    }
}
