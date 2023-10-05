package io.sseg.boundedcontext.application.entity;


import io.sseg.base.entity.UserOwnable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


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
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "email_template_id")
    private List<EmailTemplate> emailTemplates;
    
}
