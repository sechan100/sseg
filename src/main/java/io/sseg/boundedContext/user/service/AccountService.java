package io.sseg.boundedContext.user.service;


import io.sseg.boundedContext.user.entity.Account;
import io.sseg.boundedContext.user.auth.model.ProviverUser;
import io.sseg.boundedContext.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    
    // OAuth2를 이용하여 받아온 사용자 정보를 Account 엔티티 형태로 converting하여 일반적인 회원가입시에 사용하는 register 메소드로 DB저장을 위임
    public void register(ProviverUser proviverUser, OAuth2UserRequest request) {
        
        Account account = accountRepository.findByUsername(proviverUser.getUsername());
        
        if(account == null) {
            
            String registrationId = request.getClientRegistration().getRegistrationId();
            
            
            account = new Account();
            
            account.setOauthId(proviverUser.getId());
            account.setUsername(proviverUser.getUsername());
            account.setPassword(proviverUser.getPassword());
            account.setEmail(proviverUser.getEmail());
            
            
            
            accountRepository.save(account);
            
        } else {
            
            
            System.out.println("이미 존재하는 계정입니다.");
            
        }
    }
    
    
}
