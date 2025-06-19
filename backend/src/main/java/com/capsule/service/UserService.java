package com.capsule.service;

import com.capsule.dto.LoginDTO;
import com.capsule.dto.RegisterDTO;
import com.capsule.model.User;

public interface UserService {
    User registerNewUser(RegisterDTO registerDTO);
    boolean authenticateUser(LoginDTO loginDTO);
    void updateEmailReminderSetting(Long userId, boolean enabled);
    boolean getEmailReminderSetting(Long userId);
    User getUserById(Long userId);
    User getUserByUsername(String username); // ✅ Newly added
}
