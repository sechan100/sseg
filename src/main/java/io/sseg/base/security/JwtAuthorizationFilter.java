package io.sseg.base.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.sseg.base.jwt.JwtUtil;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final Rq rq;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");
        
        // validate existence of token
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String token = header.replace("Bearer ", "");
        
        // token validation
        if(!jwtUtil.validateToken(token)){
            filterChain.doFilter(request, response);
            return;
        }
        
        
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(jwtUtil.getSignKey()).build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Long appId = Long.valueOf(claims.getSubject());
        
        rq.setRequestAttr("appId", appId);
    
        filterChain.doFilter(request, response);
    }
}
