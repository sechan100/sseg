package io.sseg.boundedcontext.application.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sseg.base.entity.BaseEntity;
import io.sseg.boundedcontext.application.model.EmailTemplateDto;
import io.sseg.boundedcontext.application.service.JsonToMapConverter;
import io.sseg.boundedcontext.application.service.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Setter @Getter
@NoArgsConstructor
public class EmailTemplate extends BaseEntity {
    
    private String name;
    
    private String templateType;
    
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String template;
    
    @Convert(converter = StringListConverter.class)
    private List<String> variableNames = new ArrayList<>();
    
    @Convert(converter = JsonToMapConverter.class)
    private Map<String, Object> sampleVariables = new HashMap<>();
    
    public EmailTemplate(EmailTemplateDto dto) {
        this.name = dto.getName();
        this.templateType = dto.getTemplateType();
        this.template = dto.getTemplate();
        this.variableNames = dto.getVariableNames();
        this.sampleVariables = dto.getSampleVariables();
    }
}


