package io.sseg.boundedcontext.application.entity;


import io.sseg.base.entity.UserOwnable;
import io.sseg.boundedcontext.email.model.SMTPProperties;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends UserOwnable {
    
    private String jwtToken;
    
    private String name;
    
    private String description;
    
    private String domain;
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "host", column = @Column(name = "smtp_host")),
            @AttributeOverride(name = "port", column = @Column(name = "smtp_port")),
            @AttributeOverride(name = "username", column = @Column(name = "smtp_username")),
            @AttributeOverride(name = "password", column = @Column(name = "smtp_password")),
    })
    private SMTPProperties smtpProperties;
    
}
