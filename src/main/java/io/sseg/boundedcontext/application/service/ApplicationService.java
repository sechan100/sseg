package io.sseg.boundedcontext.application.service;


import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.entity.SMTPProperties;
import io.sseg.boundedcontext.application.model.ApplicationDto;
import io.sseg.boundedcontext.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final Rq rq;
    
    
    public List<Application> findAllByOwnerId(Long ownerId) {
        
        return applicationRepository.findByOwnerId(ownerId);
        
    }
    
    
    public Long create(ApplicationDto applicationRegistrationForm, String appId, String appSecret) {
            
        Application application = Application.builder()
                .name(applicationRegistrationForm.getName())
                .description(applicationRegistrationForm.getDescription())
                .domain(applicationRegistrationForm.getDomain())
                .owner(rq.getAccount())
                .appId(appId)
                .appSecret(appSecret)
                .smtpProperties(applicationRegistrationForm.getSmtpProperties())
                .build();
        
        applicationRepository.save(application);
        
        return application.getId();
    }
    
    public Application findById(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow();
    }
}
