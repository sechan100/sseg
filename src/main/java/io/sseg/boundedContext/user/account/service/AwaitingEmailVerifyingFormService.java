package io.sseg.boundedContext.user.account.service;

import io.sseg.boundedContext.user.account.model.dto.AwaitingEmailVerifyingRedisEntity;
import io.sseg.boundedContext.user.account.model.dto.VerifyRequestRegisterForm;
import io.sseg.boundedContext.user.account.repository.AwaitingEmailVerifyingFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
