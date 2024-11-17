package org.projectmanagement.domain.enums;


public interface EnumConstrainValidator<T> {

    boolean isValid(T value);

}
