package com.capsule.controller;

import com.capsule.dto.UserDTO;
import com.capsule.model.User;
import com.capsule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin // allows frontend to connect later
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return userService.registerUser(user);
    }
}
