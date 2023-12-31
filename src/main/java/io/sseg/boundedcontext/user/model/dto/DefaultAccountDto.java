package io.sseg.boundedcontext.user.model.dto;

import io.sseg.base.validation.annotation.Unique;
import io.sseg.boundedcontext.user.repository.AccountRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
abstract public class DefaultAccountDto implements AccountDto {
    
    @NotEmpty
    protected String provider;
    
    @NotEmpty
    protected String username;
    
    @NotEmpty
    protected String password;
    
    @Email
    protected String email;
    
}




