package io.sseg.boundedcontext.user.account.entity;

import io.sseg.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Account extends BaseEntity {
    
    private String provider;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String role;
    
    private String nickname;
    
    
}
