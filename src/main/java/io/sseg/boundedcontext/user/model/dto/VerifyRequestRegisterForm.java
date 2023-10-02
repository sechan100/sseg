package io.sseg.boundedcontext.user.model.dto;


import io.sseg.infra.ProviderType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyRequestRegisterForm extends DefaultAccountDto{
    
    private String passwordConfirm;
    
    public VerifyRequestRegisterForm() {
        super.provider = ProviderType.NATIVE;
    }
    
}
