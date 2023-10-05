package io.sseg.boundedcontext.application.entity;

import io.sseg.base.entity.BaseEntity;
import io.sseg.boundedcontext.application.model.EmailTemplateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@NoArgsConstructor
public class EmailTemplate extends BaseEntity {
    
    private String name;
    
    private String templateType;
    
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String template;
    
    public EmailTemplate(EmailTemplateDto dto) {
        this.name = dto.getName();
        this.templateType = dto.getTemplateType();
        this.template = dto.getTemplate();
    }
}
