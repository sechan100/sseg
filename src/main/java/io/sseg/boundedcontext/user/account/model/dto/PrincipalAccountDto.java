package io.sseg.boundedcontext.user.account.model.dto;

import io.sseg.boundedcontext.user.account.entity.Account;
import lombok.Data;

@Data
public class PrincipalAccountDto {
    
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String role;
    
    
    public PrincipalAccountDto(Account account){
        this.id = account.getId();
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.email = account.getEmail();
        this.role = account.getRole();
    }
}
