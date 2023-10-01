package io.sseg.boundedContext.user.account.model.dto;

import lombok.Data;


@Data
abstract public class DefaultAccountDto implements AccountDto{
    
    protected String provider;
    protected String username;
    protected String password;
    protected String email;
    
}
