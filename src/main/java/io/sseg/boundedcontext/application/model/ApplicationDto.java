package io.sseg.boundedcontext.application.model;

import io.sseg.boundedcontext.email.model.SMTPProperties;
import io.sseg.boundedcontext.user.entity.Account;

public interface ApplicationDto {
    
    String getName();
    
    String getDescription();
    
    String getDomain();
    
    Account getOwner();
    
    SMTPProperties getSmtpProperties();
}
