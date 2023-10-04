package io.sseg.boundedcontext.user.service;


import io.sseg.boundedcontext.user.model.dto.AccountDetailsRegisterForm;
import io.sseg.boundedcontext.user.model.dto.AccountDto;
import io.sseg.boundedcontext.user.repository.AwaitingEmailVerifyingFormRepository;
import io.sseg.boundedcontext.email.model.EmailRequest;
import io.sseg.boundedcontext.email.service.EmailSendService;
import io.sseg.boundedcontext.email.service.ThymeleafEmailTemplateResolver;
import io.sseg.boundedcontext.user.entity.Account;
import io.sseg.boundedcontext.user.repository.AccountRepository;
import io.sseg.base.properties.Properties;
import io.sseg.base.security.util.Role;
import io.sseg.infra.util.Ut;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    @Getter private final AccountRepository accountRepository;
    private final ThymeleafEmailTemplateResolver thymeleafEmailTemplateResolver;
    private final EmailSendService emailSendService;
    private final AwaitingEmailVerifyingFormRepository emailCacheRepository;
    private final PasswordEncoder passwordEncoder;
    private final Properties properties;
    
    
    
    // OAuth2를 이용하여 받아온 사용자 정보를 Account 엔티티 형태로 converting하여 일반적인 회원가입시에 사용하는 register 메소드로 DB저장을 위임
    public void register(@Valid AccountDetailsRegisterForm form) {
        
            Account account = Account.builder()
                    .provider(form.getProvider())
                    .email(form.getEmail())
                    .username(form.getUsername())
                    .password(passwordEncoder.encode(form.getPassword()))
                    .nickname(form.getNickname())
                    .role(Role.USER)
                    .build();
            
            accountRepository.save(account);
    }
    
    
    public String sendEmailVerifyingEmail(String toEmail, String authCode) {
        
        if(authCode == null){
            authCode = Ut.generator.generateRandomString();
        }
        
        String authenticationUrl = properties.getHost() + "/register?code=" + authCode + "&email=" + toEmail;
        
        // 인증 코드를 템플릿에 담아서 이메일 내용 생성
        Map<String, Object> variables = Map.of("authenticationUrl", authenticationUrl);
        String emailContent = thymeleafEmailTemplateResolver.resolveFile("/email/email_verify_template", variables);
        
        // 이메일 발송
        emailSendService.sendMail(new EmailRequest(properties.getEmailVerificationFromName(), toEmail, "SSEG 회원가입 이메일 인증", emailContent));
        
        
        return authCode;
    }
    
    @Transactional
    public Account updateAccountDto(AccountDto registerForm) {
            
            Account account = accountRepository.findByUsername(registerForm.getUsername());
            account.setUsername(registerForm.getUsername());
            account.setPassword(passwordEncoder.encode(registerForm.getPassword()));
            account.setProvider(registerForm.getProvider());
            account.setEmail(registerForm.getEmail());
            
            return accountRepository.save(account);
    }
    
    
    
    
    
    
    
    // ****************************************************
    // *********     JPA Repository service      **********
    // ****************************************************
    
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
    
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }
    
    public boolean existsByEmailAndProvider(String email, String provider) {
        return accountRepository.existsByEmailAndProvider(email, provider);
    }
}
