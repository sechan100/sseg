package io.sseg.boundedcontext.user.service;


import io.sseg.boundedcontext.user.exception.RegistrationException;
import io.sseg.boundedcontext.user.model.dto.AccountDetailsRegisterForm;
import io.sseg.boundedcontext.user.model.dto.AccountDto;
import io.sseg.boundedcontext.user.repository.AwaitingEmailVerifyingFormRepository;
import io.sseg.boundedcontext.email.model.EmailRequest;
import io.sseg.boundedcontext.email.service.EmailSendService;
import io.sseg.boundedcontext.email.service.ThymeleafEmailTemplateResolver;
import io.sseg.boundedcontext.user.entity.Account;
import io.sseg.boundedcontext.user.model.dto.VerifyRequestRegisterForm;
import io.sseg.boundedcontext.user.repository.AccountRepository;
import io.sseg.base.properties.Properties;
import io.sseg.base.security.util.Role;
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
                    .build();
            
            accountRepository.save(account);
            
        } else {
            
            throw new RegistrationException(RegistrationException.INVALID_USERNAME);
            
        }
    }
    
    
    // VerifyRequestRegisterForm의 아이디, 비밀번호, 이메일등의 중복 검사.
    // provider가 다른 경우, 같은 이메일, 아이디라도 다른 계정으로 판단한다.
    public void validate(VerifyRequestRegisterForm form){
        
        // username 중복 검사
        if(existsByUsername(form.getUsername(), form.getProvider())){
            throw new RegistrationException(RegistrationException.USERNAME_DUPLICATION);
        }
        
        // password 일치 검사
        if(!form.getPassword().equals(form.getPasswordConfirm())){
            throw new RegistrationException(RegistrationException.INVALID_PASSWORD_CONFIRM);
        }
        
        // email 중복 검사
        if(existsByEmailAndProvider(form.getEmail(), form.getProvider())){
            throw new RegistrationException(RegistrationException.EMAIL_DUPLICATION);
        }
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
    
    private boolean isExistUsername(String username) {
        return accountRepository.existsByUsername(username);
    }
    
    public boolean existsByUsername(String username, String provider) {
        return accountRepository.existsByUsername(username);
    }
    
    public boolean existsByEmailAndProvider(String email, String provider) {
        return accountRepository.existsByEmailAndProvider(email, provider);
    }
}
