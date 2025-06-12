package com.capsule.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5174") // Update if your frontend runs on a different port
public class UserProfileController {

    // Add /user/messages or other user-related endpoints here
}
