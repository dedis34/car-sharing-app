package dedis.carsharingapp.customAnnnotations;

import dedis.carsharingapp.customAnnnotations.validators.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail{
    String message() default "Email must be unique.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
