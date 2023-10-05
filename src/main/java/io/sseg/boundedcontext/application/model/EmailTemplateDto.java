package io.sseg.boundedcontext.application.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailTemplateDto {
    
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String templateType;
    
    @NotEmpty
    private String template;
    
    
}
