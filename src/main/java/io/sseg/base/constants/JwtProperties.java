package io.sseg.base.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class JwtProperties {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expirationPeriodMinutes}")
    private Integer expirationPeriodMinutes;
    
    @Value("${jwt.refresh.expirationPeriodDay}")
    private Integer expirationPeriodDay;
}
