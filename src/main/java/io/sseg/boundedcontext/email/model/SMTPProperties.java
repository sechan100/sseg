package io.sseg.boundedcontext.email.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
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

