package io.sseg.boundedContext.email.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.util.Map;

@Component
@RequiredArgsConstructor
public class ThymeleafEmailTemplateResolver {
    
    @Qualifier("basicTemplateEngine")
    private final TemplateEngine basicTemplateEngine;
    
    @Qualifier("htmlTemplateEngine")
    private final TemplateEngine htmlTemplateEngine;
    
    
    
    public String resolveHtml(String template, Map<String, Object> variables){
        
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
}
