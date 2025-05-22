package com.capsule.service;

import com.capsule.model.UserMessage;
import com.capsule.repository.UserMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    private final UserMessageRepository userMessageRepository;

    public UserMessageServiceImpl(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public void saveMessage(UserMessage message) {
        userMessageRepository.save(message);
    }

    @Override
    public List<UserMessage> getMessagesByUsername(String username) {
        return userMessageRepository.findByUsername(username);
    }
}
