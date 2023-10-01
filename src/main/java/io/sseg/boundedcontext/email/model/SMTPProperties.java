package io.sseg.boundedcontext.email.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class SMTPProperties {
    
    private String host;
    private String port;
    private String username;
    private String password;
    
}
