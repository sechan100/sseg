package io.sseg.boundedcontext.user.entity;

import io.sseg.base.entity.BaseEntity;
import io.sseg.boundedcontext.application.entity.Application;
import jakarta.persistence.*;
import lombok.*;

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
    
}
