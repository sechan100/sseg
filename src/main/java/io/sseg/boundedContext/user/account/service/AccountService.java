package io.sseg.boundedContext.user.account.service;


import io.sseg.boundedContext.email.model.EmailRequest;
import io.sseg.boundedContext.email.service.EmailSendService;
import io.sseg.boundedContext.email.service.ThymeleafEmailTemplateResolver;
import io.sseg.boundedContext.user.account.entity.Account;
import io.sseg.boundedContext.user.account.exception.RegistrationException;
import io.sseg.boundedContext.user.account.model.dto.AccountDetailsRegisterForm;
import io.sseg.boundedContext.user.account.model.dto.VerifyRequestRegisterForm;
import io.sseg.boundedContext.user.account.repository.AccountRepository;
import io.sseg.boundedContext.user.account.repository.AwaitingEmailVerifyingFormRepository;
import io.sseg.infra.Properties;
import io.sseg.infra.Role;
import io.sseg.infra.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final ThymeleafEmailTemplateResolver thymeleafEmailTemplateResolver;
    private final EmailSendService emailSendService;
    private final AwaitingEmailVerifyingFormRepository emailCacheRepository;
    private final PasswordEncoder passwordEncoder;
    private final Properties properties;
    
    
    
    // OAuth2를 이용하여 받아온 사용자 정보를 Account 엔티티 형태로 converting하여 일반적인 회원가입시에 사용하는 register 메소드로 DB저장을 위임
    public void register(AccountDetailsRegisterForm form) {
        
        // username 중복 검사
        boolean registerAvailability = !isExistUsername(form.getUsername());
        
        if(registerAvailability) {
            
            Account account = Account.builder()
                    .provider(form.getProvider())
                    .email(form.getEmail())
                    .username(form.getUsername())
                    .password(passwordEncoder.encode(form.getPassword()))
                    .nickname(form.getNickname())
                    .role(Role.USER)
                    .smtpProperties(form.getSmtpProperties())
                    .build();
            
            accountRepository.save(account);
            
        } else {
            
            throw new RegistrationException(RegistrationException.INVALID_USERNAME);
            
        }
    }
    
    public boolean validate(VerifyRequestRegisterForm form){
        
        // username 중복 검사
        if(isExistUsername(form.getUsername())){
            throw new RegistrationException(RegistrationException.USERNAME_DUPLICATION);
        }
        
        // password 일치 검사
        if(!form.getPassword().equals(form.getPasswordConfirm())){
            throw new RegistrationException(RegistrationException.INVALID_PASSWORD_CONFIRM);
        }
        
        // email 중복 검사
        if(isExistEmail(form.getEmail())){
            throw new RegistrationException(RegistrationException.EMAIL_DUPLICATION);
        }
        
        return true;
    }
    
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
    
    public boolean isExistUsername(String username) {
        return accountRepository.findByUsername(username) != null;
    }
    
    public boolean isExistEmail(String email) {
        return accountRepository.findByEmail(email) != null;
    }
    
    public String sendEmailVerifyingEmail(String toEmail, String authCode) {
        
        if(authCode == null){
            authCode = Ut.randomString();
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
    public void updateAccountDetails(AccountDetailsRegisterForm registerForm) {
            
            Account account = accountRepository.findByUsernameAndProvider(registerForm.getUsername(), registerForm.getProvider());
            account.setUsername(registerForm.getUsername());
            account.setPassword(passwordEncoder.encode(registerForm.getPassword()));
            account.setRole(registerForm.getRole());
            account.setProvider(registerForm.getProvider());
            account.setEmail(registerForm.getEmail());
            account.setNickname(registerForm.getNickname());
            account.setSmtpProperties(registerForm.getSmtpProperties());
            
            accountRepository.save(account);
    }
}
