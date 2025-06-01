package com.capsule.service;

import com.capsule.dto.RegisterDTO;
import com.capsule.model.User;

public interface UserService {
    User registerNewUser(RegisterDTO registerDTO);
    User getUserById(Long id);
}
