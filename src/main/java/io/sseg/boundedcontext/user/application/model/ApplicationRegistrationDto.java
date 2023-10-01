package io.sseg.boundedcontext.user.application.model;

import io.sseg.boundedcontext.email.model.SMTPProperties;
import io.sseg.boundedcontext.user.account.entity.Account;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRegistrationDto implements ApplicationDto {
    
    private String name;
    
    private String description;
    
    private String domain;
    
    private Account owner;
    
    private SMTPProperties smtpProperties;
    
}