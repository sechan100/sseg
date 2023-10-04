package io.sseg.boundedcontext.user.model.dto;

import io.sseg.boundedcontext.user.entity.Account;
import io.sseg.base.security.Role;
import lombok.Data;

@Data
public class AccountPrincipal {
    
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String role;
    private boolean login = false;
    private boolean admin = false;
    
    
    public AccountPrincipal(Account account){
        
        this.id = account.getId();
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.email = account.getEmail();
        this.role = account.getRole();
        
        if(this.role.equals(Role.ADMIN)) {
            admin = true;
        }
        
    }
}
