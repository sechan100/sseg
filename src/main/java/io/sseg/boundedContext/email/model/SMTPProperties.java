package io.sseg.boundedContext.email.model;

import lombok.Data;

@Data
public class SMTPProperties {
    
    private String host;
    private String port;
    private String username;
    private String password;
    
}
