package io.sseg.boundedcontext.application.model;

import io.sseg.boundedcontext.email.model.SMTPProperties;
import io.sseg.boundedcontext.user.model.dto.AccountPrincipal;
import io.sseg.infra.ioc.SpringContext;
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
    
    @NotNull
    private AccountPrincipal owner = SpringContext.getAccountPrincipal();
    
    @Valid
    @Setter
    private SMTPProperties smtpProperties;
    
}
