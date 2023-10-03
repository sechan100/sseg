package io.sseg.base.validation.annotation;

import io.sseg.base.validation.validator.UniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.data.repository.CrudRepository;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Unique {
    
    String message() default "default error message";
    
    Class<?>[] groups() default { };
    
    Class<? extends Payload>[] payload() default { };
    
    String columnName();
    
    Class<? extends CrudRepository<?, ?>> repository();
}