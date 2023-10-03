package io.sseg.boundedcontext.application.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class SMTPProperties {
    
    @NotEmpty
    private String host;
    
    @Positive
    private Integer port;
    
    @NotEmpty
    private String username;
    
    @NotEmpty
    private String password;
    
}