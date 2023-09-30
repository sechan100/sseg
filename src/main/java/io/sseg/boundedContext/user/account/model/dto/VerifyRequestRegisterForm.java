package io.sseg.boundedContext.user.account.model.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyRequestRegisterForm extends DefaultAccountDto{
    
    private String passwordConfirm;
    
}
