package com.capsule.repository;

import com.capsule.model.User;
import com.capsule.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    List<UserMessage> findByUser(User user);
    List<UserMessage> findByUserId(Long userId);
    List<UserMessage> findByUserUsername(String username);
    List<UserMessage> findByUserAndMessageDateTimeAfter(User user, LocalDateTime dateTime);
    List<UserMessage> findByMessageDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Optional<UserMessage> findByIdAndUserId(Long messageId, Long userId); // ✅ for security


    List<UserMessage> findByUserAndMessageDateTimeBetween(User user, LocalDateTime start, LocalDateTime end);

    Optional<UserMessage> findByShareToken(String shareToken);

}
