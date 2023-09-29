package io.sseg.boundedContext.email;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    
    private String from;
    private String to;
    private String subject;
    private String text;
    
}