package io.sseg.base.security;

import io.sseg.boundedcontext.user.service.CustomOAuth2UserService;
import io.sseg.boundedcontext.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class OAuth2ClientConfig {
    
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/verify/email", "/error*").permitAll()
                        .anyRequest().authenticated()
                )
                
                .formLogin(form -> form
                        .loginPage("/login")
                )
                
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .loginPage("/login")
                )
                
                .csrf(AbstractHttpConfigurer::disable)
                
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/").permitAll()
                );
        
        
        return http.build();
    }
    
    // static resource allow
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**");
    }
 
    
}
