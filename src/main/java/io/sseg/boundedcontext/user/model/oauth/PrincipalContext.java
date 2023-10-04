package io.sseg.boundedcontext.user.model.oauth;


import io.sseg.base.security.Role;
import lombok.Getter;
import io.sseg.boundedcontext.user.entity.Account;
import io.sseg.boundedcontext.user.model.dto.AccountPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.Collection;
import java.util.Map;


public class PrincipalContext extends User implements OAuth2User {
    
    @Getter
    protected AccountPrincipal account;
    
    public PrincipalContext(Account account) {
        super(account.getUsername(), account.getPassword(), Role.getAuthorities(account.getRole()));
        this.account = new AccountPrincipal(account);
    }
    
    public PrincipalContext(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
    
    @Override
    public String getName() {
        return null;
    }
    
}
