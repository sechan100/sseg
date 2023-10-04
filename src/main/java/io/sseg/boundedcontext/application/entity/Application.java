package io.sseg.boundedcontext.application.entity;


import io.sseg.base.entity.UserOwnable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Application extends UserOwnable {
    
    private String name;
    
    private String description;
    
    private String domain;
    
    private String appId;
    
    private String appSecret;
    
    private String refreshToken;
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "host", column = @Column(name = "smtp_host")),
            @AttributeOverride(name = "port", column = @Column(name = "smtp_port")),
            @AttributeOverride(name = "username", column = @Column(name = "smtp_username")),
            @AttributeOverride(name = "password", column = @Column(name = "smtp_password")),
    })
    private SMTPProperties smtpProperties;
    
}
