package org.projectmanagement.domain.enums;

import org.apache.commons.lang3.EnumUtils;

public enum DefaultStatus implements EnumConstrainValidator<String> {
    BACKLOG, TODO, IN_PROGRESS, DONE, CANCELLED;;

    @Override
    public boolean isValid(String value) {
        return EnumUtils.isValidEnum(this.getDeclaringClass(), value);
    }
}
