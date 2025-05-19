package com.capsule.repository;

import com.capsule.model.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    List<Capsule> findByUsername(String username);
}
