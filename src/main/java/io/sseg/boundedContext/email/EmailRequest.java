package io.sseg.boundedContext.email;


import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class EmailRequest {
    private final String from;
    private final String to;
    private final String subject;
    private final String text;
    
}