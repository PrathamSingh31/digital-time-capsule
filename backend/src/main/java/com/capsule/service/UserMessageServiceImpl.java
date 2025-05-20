package com.capsule.service;

import com.capsule.model.UserMessage;
import com.capsule.repository.UserMessageRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    private final UserMessageRepository userMessageRepository;

    public UserMessageServiceImpl(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public void saveMessage(String username, String message, LocalDate unlockDate) {
        UserMessage userMessage = new UserMessage();
        userMessage.setUsername(username);
        userMessage.setMessage(message);
        userMessage.setUnlockDate(unlockDate);
        userMessageRepository.save(userMessage);
    }

    @Override
    public List<UserMessage> getMessagesByUsername(String username) {
        return userMessageRepository.findByUsername(username);
    }
}
