package io.sseg.boundedContext.email;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Map;

@RequiredArgsConstructor
public class ThymeleafEmailTemplateResolver {
    
    @Autowired
    private TemplateEngine templateEngine;
    
    private final String template;
    private final Map<String, Object> variables;
    
    
    
    
    public String resolve(){
        
        Context context = new Context();
        
        for(String key : variables.keySet()){
            context.setVariable(key, variables.get(key));
        }
        
        context.setVariables(variables);
        
        return templateEngine.process(template, context);
    }
    
}
