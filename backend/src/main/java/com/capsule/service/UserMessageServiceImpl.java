package com.capsule.service;

import com.capsule.model.User;
import com.capsule.model.UserMessage;
import com.capsule.dto.MessageRequest;
import com.capsule.repository.UserMessageRepository;
import com.capsule.repository.UserRepository;
import com.capsule.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserMessage createMessage(Long userId, MessageRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        UserMessage message = new UserMessage();
        message.setUser(user);  // sets both user and userId
        message.setTitle(request.getTitle());
        message.setContent(request.getContent());
        message.setDeliveryDate(request.getDeliveryDate());
        message.setCreatedAt(LocalDateTime.now());

        return userMessageRepository.save(message);
    }

    @Override
    public List<UserMessage> getMessagesByUserId(Long userId) {
        return userMessageRepository.findByUserId(userId);
    }
}
