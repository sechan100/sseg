package io.sseg.boundedContext.user.account.model.dto;


import io.sseg.boundedContext.email.model.SMTPProperties;
import io.sseg.boundedContext.user.account.model.oauthuser.ProviderUser;
import io.sseg.infra.Role;
import io.sseg.infra.ProviderType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDetailsRegisterForm extends DefaultAccountDto {
    
    private String nickname;
    private SMTPProperties smtpProperties;
    private boolean useBuiltInSmtp = false;
    private String role;
    
    
    
    public AccountDetailsRegisterForm(){
         provider = ProviderType.NATIVE;
    }
    
    public AccountDetailsRegisterForm(ProviderUser form){
        super.username = form.getUsername();
        super.password = form.getPassword();
        super.email = form.getEmail();
        super.provider = form.getProvider();
        this.role = Role.USER;
    }
    
    public AccountDetailsRegisterForm(AccountDto form){
        super.username = form.getUsername();
        super.password = form.getPassword();
        super.email = form.getEmail();
        super.provider = form.getProvider();
        this.role = Role.USER;
    }
    
    public AccountDetailsRegisterForm(AwaitingEmailVerifyingRedisEntity form){
        super.username = form.getUsername();
        super.password = form.getPassword();
        super.email = form.getEmail();
        super.provider = form.getProvider();
        this.role = Role.USER;
    }
    
}
