package org.projectmanagement.application.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OwnersRead(
    @NotNull(message = "cannot be null")
    UUID id,

    @NotBlank(message = "cannot be blank")
    String name,

    @NotBlank(message = "cannot be blank")
    String email
) {

}
