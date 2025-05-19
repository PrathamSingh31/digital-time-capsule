package com.capsule.service;

import com.capsule.model.Capsule;
import java.util.List;

public interface CapsuleService {
    Capsule createCapsule(String message, String username, String unlockDate);
    List<Capsule> getAccessibleCapsules(String username);
}
