package io.sseg.boundedcontext.application.service;


import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.entity.EmailTemplate;
import io.sseg.boundedcontext.application.model.ApplicationDto;
import io.sseg.boundedcontext.application.model.EmailTemplateDto;
import io.sseg.boundedcontext.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    
    public Application findByAppId(String appId) {
        return applicationRepository.findByAppId(appId);
    }
    
    @Transactional
    public void saveRefreshToken(String appId, String refreshToken) {
        Application application = findByAppId(appId);
        application.setRefreshToken(refreshToken);
    }
    
    @Transactional
    public boolean addEmailTemplate(String appId, EmailTemplateDto templateForm) {
        
        Application application = findByAppId(appId);
        List<EmailTemplate> emailTemplates = application.getEmailTemplates();
        boolean isExistName = emailTemplates.stream().anyMatch(emailTemplate -> emailTemplate.getName().equals(templateForm.getName()));
        
        if(isExistName){
            return false;
        } else {
            emailTemplates.add(new EmailTemplate(templateForm));
            return true;
        }
    }
    
    @Transactional
    public boolean removeEmailTemplate(String appId, String templateName) {
        Application application = findByAppId(appId);
        List<EmailTemplate> emailTemplates = application.getEmailTemplates();
        Optional<EmailTemplate> templateOptional = emailTemplates.stream().filter(emailTemplate -> emailTemplate.getName().equals(templateName)).findAny();
        
        if(templateOptional.isPresent()){
            EmailTemplate emailTemplate = templateOptional.get();
            emailTemplates.remove(emailTemplate);
            return true;
        } else {
            return false;
        }
        
    }
}
