package io.sseg.boundedcontext.email.service;


import io.sseg.boundedcontext.application.exception.TemplateParsingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ThymeleafEmailTemplateResolver {
    
    @Qualifier("basicTemplateEngine")
    private final TemplateEngine basicTemplateEngine;
    
    @Qualifier("htmlTemplateEngine")
    private final TemplateEngine htmlTemplateEngine;
    
    
    
    public String resolveHtml(String template, Map<String, Object> variables, List<String> variableNames) throws TemplateParsingException {
        
        validateVariables(variables, variableNames);
        
        Context context = new Context();
        
        for(String key : variables.keySet()){
            context.setVariable(key, variables.get(key));
        }
        
        context.setVariables(variables);
        
        return htmlTemplateEngine.process(template, context);
    }
    
    public String resolveFile(String template, Map<String, Object> variables){
        
        Context context = new Context();
        
        for(String key : variables.keySet()){
            context.setVariable(key, variables.get(key));
        }
        
        context.setVariables(variables);
        
        return basicTemplateEngine.process(template, context);
    }
    
    public void validateVariables(Map<String, Object> variables, List<String> variableNames) throws TemplateParsingException {
        
        Set<String> variableNameSet = new HashSet<>(variableNames);
        Set<String> variableKeySet = variables.keySet();
        
        if (!variableNameSet.equals(variableKeySet)) {
            
            // variableNames에만 있는 요소 찾기
            Set<String> onlyInVariableNames = new HashSet<>(variableNameSet);
            onlyInVariableNames.removeAll(variableKeySet);
            if (!onlyInVariableNames.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String omitedVariables : onlyInVariableNames) {
                    sb.append("'").append(omitedVariables).append("'").append(", ");
                }
                throw new TemplateParsingException("템플릿에 필요한 변수가 누락되었습니다: " + sb.toString());
            }
            
            // variables에만 있는 키 찾기
            Set<String> onlyInVariables = new HashSet<>(variableKeySet);
            onlyInVariables.removeAll(variableNameSet);
            if (!onlyInVariables.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String invalidVariables : onlyInVariables) {
                    sb.append("'").append(invalidVariables).append("'").append(", ");
                }
                throw new TemplateParsingException("VariableNames로 등록하지 않은 변수가 존재합니다: " + sb.toString());
            }
        }
    }

}

