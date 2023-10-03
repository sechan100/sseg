package io.sseg.boundedcontext.application.model;

import io.sseg.boundedcontext.application.entity.SMTPProperties;
import io.sseg.boundedcontext.user.model.dto.AccountPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ApplicationRegistrationDto implements ApplicationDto {
    
    @NotEmpty
    @Setter
    private String name;
    
    @NotNull
    @Setter
    private String description;
    
    @NotEmpty
    @Setter
    private String domain;
    
    private AccountPrincipal owner;
    
    @Valid
    @Setter
    private SMTPProperties smtpProperties;
    
}
