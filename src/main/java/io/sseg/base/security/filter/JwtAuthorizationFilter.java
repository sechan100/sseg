package io.sseg.base.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.sseg.base.http.ApiResponse;
import io.sseg.base.http.SsegApiResponseStatus;
import io.sseg.base.jwt.JwtProvider;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.jwt.model.JwtTokenDto;
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
    
    private final JwtProvider jwtProvider;
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
        try {
            if(!jwtProvider.validateToken(token)){
                filterChain.doFilter(request, response);
                return;
            }
        } catch(ExpiredJwtException e) {
            ObjectMapper jsonMapper = new ObjectMapper();
            
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(jsonMapper.writeValueAsString(new ApiResponse<>(null, SsegApiResponseStatus.TOKEN_EXPIRED, SsegApiResponseStatus.TOKEN_EXPIRED.detail)));
            return;
        }
        
        rq.setJwtToken(new JwtTokenDto(token, jwtProvider.getSignKey()));
    
        filterChain.doFilter(request, response);
    }
}
