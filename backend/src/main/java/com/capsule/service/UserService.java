package com.capsule.service;

import com.capsule.model.User;
import com.capsule.model.UserMessage;

import java.util.List;

public interface UserService {

    User registerUser(User user);

    boolean authenticateUser(String username, String password);

    String loginUser(String username, String password);

    User findByUsername(String username);

    UserMessage saveUserMessage(UserMessage message);

    List<UserMessage> getMessagesByUsername(String username);
}
