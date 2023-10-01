package io.sseg.boundedContext.user.account.entity;

import io.sseg.boundedContext.email.model.SMTPProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String provider;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String role;
    
    private String nickname;
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "host", column = @Column(name = "smtp_host")),
            @AttributeOverride(name = "port", column = @Column(name = "smtp_port")),
            @AttributeOverride(name = "username", column = @Column(name = "smtp_username")),
            @AttributeOverride(name = "password", column = @Column(name = "smtp_password")),
    })
    private SMTPProperties smtpProperties;
    
}
