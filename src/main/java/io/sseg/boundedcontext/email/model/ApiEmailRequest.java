package io.sseg.boundedcontext.email.model;


import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiEmailRequest {
    
    private EmailRequest emailRequest;
    private String templateName;
    private HashMap<String, Object> templateArgs;
    
}