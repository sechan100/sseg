package io.sseg.boundedcontext.application.model;

import io.sseg.boundedcontext.email.model.SMTPProperties;
import io.sseg.boundedcontext.user.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRegistrationDto implements ApplicationDto {
    
    @NotEmpty
    private String name;
    
    private String description;
    
    @Email
    private String domain;
    
    private Account owner;
    
    private SMTPProperties smtpProperties;
    
    
}
