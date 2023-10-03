package io.sseg.boundedcontext.application.model;

import io.sseg.boundedcontext.application.entity.SMTPProperties;
import io.sseg.boundedcontext.user.model.dto.AccountPrincipal;

public interface ApplicationDto {
    
    String getName();
    
    String getDescription();
    
    String getDomain();
    
    AccountPrincipal getOwner();
    
    SMTPProperties getSmtpProperties();
}
