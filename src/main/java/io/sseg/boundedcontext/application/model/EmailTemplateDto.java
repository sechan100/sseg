package io.sseg.boundedcontext.application.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EmailTemplateDto {
    
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String templateType;
    
    @NotEmpty
    private String template;
    
    @NotEmpty
    private Map<String, Object> sampleVariables;
    
    @NotEmpty
    private List<String> variableNames;
    
    
}
