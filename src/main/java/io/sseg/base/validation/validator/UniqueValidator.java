package io.sseg.base.validation.validator;

import io.sseg.base.validation.annotation.Unique;
import io.sseg.infra.ioc.SpringContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {
    
    
    private CrudRepository<?, ?> repository;
    private String columnName;
    
    @Override
    public void initialize(Unique constraintAnnotation) {
        this.repository = SpringContext.getBean(constraintAnnotation.repository());
        this.columnName = constraintAnnotation.columnName();
        
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        
        if (value == null) return true;
        
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.format("The '%s' field does not satisfy the unique constraint. [%s] is already exist.", columnName, value))
                .addConstraintViolation();
        
        
        Method method;
        
        try {
            
            method = repository.getClass().getMethod("existsBy" + StringUtils.capitalize(columnName), value.getClass());
            
            try {
                boolean isExists = (boolean) method.invoke(repository, value);
                return !isExists;
            } catch (Exception e) {
                throw new RuntimeException("'existsBy' method can't invoked", e);
            }
            
            
        } catch (NoSuchMethodException e) {
            
            try {
                
                method = repository.getClass().getMethod("findBy" + StringUtils.capitalize(columnName), value.getClass());
                
                try {
                    Object findedEntity = method.invoke(repository, value);
                    return (findedEntity == null);
                } catch (Exception exception) {
                    throw new RuntimeException("'findBy' method can't invoked", e);
                }
                
                
            } catch(NoSuchMethodException ex) {
                throw new RuntimeException("can't find repository method: you must implement 'findBy' or 'existsBy'");
            }
            
        }
    }
}
