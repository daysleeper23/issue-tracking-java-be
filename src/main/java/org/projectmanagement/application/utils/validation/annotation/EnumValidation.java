package org.projectmanagement.application.utils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.projectmanagement.application.utils.validation.EnumValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface EnumValidation {

    Class<? extends Enum<?>> target();

    String message() default "invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
