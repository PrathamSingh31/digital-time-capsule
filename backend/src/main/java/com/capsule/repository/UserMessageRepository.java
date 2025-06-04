package com.capsule.repository;

import com.capsule.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    List<UserMessage> findByUserId(Long userId);

    @Query("SELECT m FROM UserMessage m WHERE m.userId = :userId AND YEAR(m.deliveryDate) = :year ORDER BY m.deliveryDate ASC")
    List<UserMessage> findByUserIdAndYearOrderByDeliveryDateAsc(Long userId, Integer year);

    @Query("SELECT m FROM UserMessage m WHERE m.userId = :userId AND YEAR(m.deliveryDate) = :year ORDER BY m.deliveryDate DESC")
    List<UserMessage> findByUserIdAndYearOrderByDeliveryDateDesc(Long userId, Integer year);

    @Query("SELECT m FROM UserMessage m WHERE m.userId = :userId AND YEAR(m.deliveryDate) = :year")
    List<UserMessage> findByUserIdAndYear(Long userId, Integer year);

    List<UserMessage> findByUserIdOrderByDeliveryDateAsc(Long userId);

    List<UserMessage> findByUserIdOrderByDeliveryDateDesc(Long userId);
}
