package io.sseg.base.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class CustomProperties {
    
    @Value("${custom.site.base-url}")
    private String host;
    
    @Value("${custom.email.from.email-verification}")
    private String emailVerificationFromName;
    
    @Value("${custom.admin.contact-email}")
    private String contactEmail;
}
