package org.projectmanagement.application.dto.users;

import jakarta.validation.constraints.NotBlank;

public record OwnersCreate (
        @NotBlank(message = "cannot be blank")
        String name,

        @NotBlank(message = "cannot be blank")
        String email,

        @NotBlank(message = "cannot be blank")
        String passwordHash
) {
}