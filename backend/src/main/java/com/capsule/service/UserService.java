package com.capsule.service;

import com.capsule.model.User;

public interface UserService {
    User registerUser(User user);
    boolean authenticateUser(String username, String password);
    String loginUser(String username, String password);
}