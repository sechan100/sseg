package io.sseg.boundedContext.email;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Map;

@Component
public class ThymeleafEmailTemplateResolver {
    
    
    @Autowired
    @Qualifier("htmlTemplateEngine")
    private TemplateEngine templateEngine;
    
    
    
    public String resolve(String template, Map<String, Object> variables){
        
        Context context = new Context();
        
        for(String key : variables.keySet()){
            context.setVariable(key, variables.get(key));
        }
        
        context.setVariables(variables);
        
        return templateEngine.process(template, context);
    }
}
