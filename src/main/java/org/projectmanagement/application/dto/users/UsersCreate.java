package org.projectmanagement.application.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.*;

import java.util.UUID;

public record UsersCreate (
    @NotBlank(message = "cannot be blank")
    String name,

    @NotBlank(message = "cannot be blank")
    String email,

    @NotBlank(message = "cannot be blank")
    String passwordHash,

    String title,

    @NotNull(message = "cannot be null")
    Boolean isActive,

    @NotNull(message = "cannot be null")
    UUID companyId,

    @NotNull(message = "cannot be null")
    Boolean isOwner
    ) {
}
