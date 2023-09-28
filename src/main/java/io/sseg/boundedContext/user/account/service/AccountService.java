package io.sseg.boundedContext.user.account.service;


import io.sseg.boundedContext.user.account.entity.Account;
import io.sseg.boundedContext.user.auth.model.ProviverUser;
import io.sseg.boundedContext.user.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    
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
    
    
    public Account findByUsername(String username) {
        
        return accountRepository.findByUsername(username);

    }
    
    public boolean isExistUsername(String username) {
        
        return accountRepository.findByUsername(username) != null;
        
    }
    
    public boolean isExistEmail(String email) {
        
        return accountRepository.findByEmail(email) != null;
        
    }
}
