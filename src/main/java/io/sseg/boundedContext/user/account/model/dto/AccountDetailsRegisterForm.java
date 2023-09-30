package io.sseg.boundedContext.user.account.model.dto;


import io.sseg.boundedContext.email.model.SMTPProperties;
import io.sseg.infra.SocialType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDetailsRegisterForm extends DefaultAccountDto {
    
    private String socialType;
    private String nickname;
    private SMTPProperties smtpProperties;
    private boolean useBuiltInSmtp = false;
    
    
    
    
    public AccountDetailsRegisterForm(AccountDto form){
        super.username = form.getUsername();
        super.password = form.getPassword();
        super.email = form.getEmail();
        socialType = SocialType.NATIVE;
    }
    
    public AccountDetailsRegisterForm(AwaitingEmailVerifyingRedisEntity form){
        super.username = form.getUsername();
        super.password = form.getPassword();
        super.email = form.getEmail();
        socialType = SocialType.NATIVE;
    }
    
}
