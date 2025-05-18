package com.capsule.repository;

import com.capsule.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (used for login or duplicate check)
    Optional<User> findByUsername(String username);

    // Find user by email (used for login or duplicate check)
    Optional<User> findByEmail(String email);

    // Check if username already exists (to prevent duplicates during registration)
    Boolean existsByUsername(String username);

    // Check if email already exists
    Boolean existsByEmail(String email);
}
