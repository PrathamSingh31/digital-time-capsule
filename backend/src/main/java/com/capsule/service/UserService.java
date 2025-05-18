package com.capsule.service;

import com.capsule.model.User;
import com.capsule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // TODO: add validation, password hashing in future
        return userRepository.save(user);
    }
}
