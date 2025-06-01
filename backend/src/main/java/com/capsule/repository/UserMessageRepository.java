package com.capsule.repository;

import com.capsule.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    // Existing methods
    List<UserMessage> findByUserUsernameOrderByCreatedAtAsc(String username);

    @Query("SELECT m FROM UserMessage m WHERE m.user.username = :username AND FUNCTION('YEAR', m.createdAt) = :year ORDER BY m.createdAt ASC")
    List<UserMessage> findByUserUsernameAndYearOrderByCreatedAtAsc(@Param("username") String username, @Param("year") Integer year);

    // ✅ New methods for filtering/sorting by userId

    List<UserMessage> findByUserId(Long userId);

    @Query("SELECT m FROM UserMessage m WHERE m.user.id = :userId AND FUNCTION('YEAR', m.createdAt) = :year")
    List<UserMessage> findByUserIdAndYear(@Param("userId") Long userId, @Param("year") Integer year);

    List<UserMessage> findByUserIdOrderByDeliveryDateAsc(Long userId);

    List<UserMessage> findByUserIdOrderByDeliveryDateDesc(Long userId);

    @Query("SELECT m FROM UserMessage m WHERE m.user.id = :userId AND FUNCTION('YEAR', m.createdAt) = :year ORDER BY m.deliveryDate ASC")
    List<UserMessage> findByUserIdAndYearOrderByDeliveryDateAsc(@Param("userId") Long userId, @Param("year") Integer year);

    @Query("SELECT m FROM UserMessage m WHERE m.user.id = :userId AND FUNCTION('YEAR', m.createdAt) = :year ORDER BY m.deliveryDate DESC")
    List<UserMessage> findByUserIdAndYearOrderByDeliveryDateDesc(@Param("userId") Long userId, @Param("year") Integer year);
}
