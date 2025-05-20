package com.capsule.repository;

import com.capsule.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    List<UserMessage> findByUsername(String username);
}
