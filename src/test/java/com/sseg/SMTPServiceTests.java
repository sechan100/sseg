package com.sseg;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Map;

@SpringBootTest
@ActiveProfiles(value = {"local"})
public class SMTPServiceTests {
    
    @Autowired
    SMTPService amazonSMTPService;
    
    @Test
    @DisplayName("Amazon SES로 이메일 전송하기")
    void send() {
        String subject = "제목입니다.";
        String to = "sechan100@gmail.com";
        Map<String, Object> variables = Map.of("data", "안녕하세요");
        
        amazonSMTPService.send(subject, variables, to);
    }
}