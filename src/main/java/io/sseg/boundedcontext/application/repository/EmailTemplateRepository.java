package io.sseg.boundedcontext.application.repository;

import io.sseg.boundedcontext.application.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    
    EmailTemplate findByName(String name);
    
}
