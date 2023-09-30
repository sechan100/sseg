package io.sseg.infra;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Properties {
    
    @Value("${custom.site.base-url}")
    private String host;
    
    @Value("${custom.email.from.email-verification}")
    private String emailVerificationFromName;
    
}
