package com.capsule.repository;

import com.capsule.model.User;
import com.capsule.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    // Find messages by User entity
    List<UserMessage> findByUser(User user);

    // Find messages by User ID
    List<UserMessage> findByUserId(Long userId);

    // Find messages by username (property of User)
    List<UserMessage> findByUserUsername(String username);

}
