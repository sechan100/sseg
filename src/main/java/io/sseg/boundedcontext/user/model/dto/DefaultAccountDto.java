package io.sseg.boundedcontext.user.model.dto;

import io.sseg.base.validation.annotation.Unique;
import io.sseg.boundedcontext.user.repository.AccountRepository;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;


@Data
abstract public class DefaultAccountDto implements AccountDto {
    
    protected String provider;
    
    @Unique(repository = AccountRepository.class, columnName = "username")
    protected String username;
    
    protected String password;
    
    @Unique(repository = AccountRepository.class, columnName = "email")
    protected String email;
    
}
