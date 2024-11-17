package org.projectmanagement.application.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.projectmanagement.application.utils.validation.annotation.EnumValidation;
import org.projectmanagement.domain.enums.EnumConstrainValidator;

import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<EnumValidation, String> {


    private Enum<?>[] valueList;

    private static BiPredicate<? super Enum<?>, String> defaultComparison =
            (currentEnumValue, testValue) -> currentEnumValue.toString().equals(testValue);

    private String errorMessage = "invalid value";

    @Override
    public void initialize(EnumValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        Class<? extends Enum<?>> enumClass = constraintAnnotation.target();
        valueList = enumClass.getEnumConstants();
        errorMessage = constraintAnnotation.message();
        if (EnumConstrainValidator.class.isAssignableFrom(enumClass)) {
            defaultComparison = (currentEnumValue, testValue) -> ((EnumConstrainValidator) currentEnumValue).isValid(testValue);
        }

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // if the value is null, it is valid
        if (null == s) {
            return true;
        }
        boolean isValid = Stream.of(valueList).anyMatch(enumValue -> defaultComparison.test(enumValue, s));
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();
        }
        return isValid;
    }
}
