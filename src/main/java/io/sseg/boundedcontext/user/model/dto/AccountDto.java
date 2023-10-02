package io.sseg.boundedcontext.user.model.dto;

public interface AccountDto {
    
    String getProvider();
    String getUsername();
    String getPassword();
    String getEmail();
}
