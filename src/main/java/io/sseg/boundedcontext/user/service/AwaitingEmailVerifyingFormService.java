package io.sseg.boundedcontext.user.service;

import io.sseg.boundedcontext.user.model.dto.AwaitingEmailVerifyingRedisEntity;
import io.sseg.boundedcontext.user.repository.AwaitingEmailVerifyingFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwaitingEmailVerifyingFormService {
    
    private final AwaitingEmailVerifyingFormRepository emailCacheRepository;
    
    public void save(AwaitingEmailVerifyingRedisEntity form) {
        emailCacheRepository.save(form);
    }
    
    public boolean existsById(String email) {
        return emailCacheRepository.existsById(email);
    }
    
    public AwaitingEmailVerifyingRedisEntity findById(String email) {
        return emailCacheRepository.findById(email).orElseThrow();
    }
}
