package io.sseg.boundedcontext.user.service;

import io.sseg.boundedcontext.user.entity.Account;
import io.sseg.boundedcontext.user.model.oauth.PrincipalContext;
import io.sseg.boundedcontext.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final AccountRepository accountRepository;
    
    @Override
    public PrincipalContext loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Account user = accountRepository.findByUsername(username);
        
        // username not found check
        if(user == null) {
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        }
        
        return new PrincipalContext(user);
    }
}