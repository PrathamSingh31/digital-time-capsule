package com.capsule.service;

import com.capsule.model.Capsule;
import com.capsule.repository.CapsuleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CapsuleServiceImpl implements CapsuleService {

    private final CapsuleRepository capsuleRepository;

    public CapsuleServiceImpl(CapsuleRepository capsuleRepository) {
        this.capsuleRepository = capsuleRepository;
    }

    @Override
    public Capsule createCapsule(String message, String username, String unlockDate) {
        Capsule capsule = new Capsule();
        capsule.setMessage(message);
        capsule.setUnlockDate(LocalDate.parse(unlockDate));
        capsule.setUsername(username);
        return capsuleRepository.save(capsule);
    }

    @Override
    public List<Capsule> getAccessibleCapsules(String username) {
        List<Capsule> capsules = capsuleRepository.findByUsername(username);
        LocalDate today = LocalDate.now();
        return capsules.stream()
                .filter(c -> !c.getUnlockDate().isAfter(today)) // only unlocked
                .collect(Collectors.toList());
    }
}
