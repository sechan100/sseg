package io.sseg.boundedContext.email;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailMsg {
    
    private String to;
    private String subject;
    private String message;
}