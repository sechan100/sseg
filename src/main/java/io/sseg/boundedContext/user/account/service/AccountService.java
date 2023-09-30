package io.sseg.boundedContext.user.account.service;


import io.sseg.boundedContext.email.model.EmailRequest;
import io.sseg.boundedContext.email.service.EmailSendService;
import io.sseg.boundedContext.email.service.ThymeleafEmailTemplateResolver;
import io.sseg.boundedContext.user.account.entity.Account;
import io.sseg.boundedContext.user.account.model.oauth.ProviverUser;
import io.sseg.boundedContext.user.account.model.dto.VerifyRequestRegisterForm;
import io.sseg.boundedContext.user.account.repository.AccountRepository;
import io.sseg.boundedContext.user.account.repository.AwaitingEmailVerifyingFormRepository;
import io.sseg.infra.Properties;
import io.sseg.infra.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final ThymeleafEmailTemplateResolver thymeleafEmailTemplateResolver;
    private final EmailSendService emailSendService;
    private final AwaitingEmailVerifyingFormRepository emailCacheRepository;
    private final Properties properties;
    
    
    
    // OAuth2를 이용하여 받아온 사용자 정보를 Account 엔티티 형태로 converting하여 일반적인 회원가입시에 사용하는 register 메소드로 DB저장을 위임
    public void register(ProviverUser proviverUser, OAuth2UserRequest request) {
        
        boolean registerAvailability = !isExistUsername(proviverUser.getUsername());
        
        if(registerAvailability) {
            
            String registrationId = request.getClientRegistration().getRegistrationId();
            
            
            Account account = new Account();
            
            account.setOauthId(proviverUser.getId());
            account.setUsername(proviverUser.getUsername());
            account.setPassword(proviverUser.getPassword());
            account.setEmail(proviverUser.getEmail());
            
            
            
            accountRepository.save(account);
            
        } else {
            
            
            System.out.println("이미 존재하는 계정입니다.");
            
        }
    }
    
    public boolean validate(VerifyRequestRegisterForm form){
        
        // username 중복 검사
        if(isExistUsername(form.getUsername())){
            return false;
        }
        
        // password 일치 검사
        if(!form.getPassword().equals(form.getPasswordConfirm())){
            return false;
        }
        
        // email 중복 검사
        if(isExistEmail(form.getEmail())){
            return false;
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
    
    
    
    
    
}
