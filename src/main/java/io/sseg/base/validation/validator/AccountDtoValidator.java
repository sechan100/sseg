package io.sseg.base.validation.validator;

import io.sseg.boundedcontext.user.exception.AccountValidationRules;
import io.sseg.boundedcontext.user.model.dto.AccountDto;
import io.sseg.boundedcontext.user.model.dto.VerifyRequestRegisterForm;
import io.sseg.boundedcontext.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
public class AccountDtoValidator implements Validator {
    
    private final AccountRepository accountRepository;
    
    
    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDto.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        AccountDto account = (AccountDto) target;
        
        // username 중복검사
        if(accountRepository.existsByUsername(account.getUsername())) {
            errors.rejectValue("username", "username", AccountValidationRules.ViolationType.USERNAME_DUPLICATION);
        }
        
        // 사용할 수 없는 username
        if(AccountValidationRules.Constraints.getInvalidUsernames().contains(account.getUsername())) {
            errors.rejectValue("username", "username", AccountValidationRules.ViolationType.INVALID_USERNAME);
        }
        
        
        // email 중복검사 (provider 별로..)
        if(accountRepository.existsByEmailAndProvider(account.getEmail(), account.getProvider())) {
            errors.rejectValue("email", "email", AccountValidationRules.ViolationType.EMAIL_DUPLICATION);
        }
        
        // VerifyRequestRegisterForm타입인 경우, passwordConfirm 일치검사
        if(account instanceof VerifyRequestRegisterForm verifyRequestRegisterForm) {
            if(!verifyRequestRegisterForm.getPassword().equals(verifyRequestRegisterForm.getPasswordConfirm())) {
                errors.rejectValue("passwordConfirm", "passwordConfirm", AccountValidationRules.ViolationType.INVALID_PASSWORD_CONFIRM);
            }
        }
        
    }
}
