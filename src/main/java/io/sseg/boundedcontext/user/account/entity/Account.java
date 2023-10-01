package io.sseg.boundedcontext.user.account.entity;

import io.sseg.base.entity.BaseEntity;
import io.sseg.boundedcontext.user.application.entity.Application;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;
    
    
    
    
    
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role));
        
        return authorities;
    }
}
