package io.sseg.boundedcontext.email.model;


import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiEmailRequest {
    
    private EmailRequest emailRequest;
    private String templateName;
    private Map<String, Object> templateArgs;
    
}