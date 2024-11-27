package org.projectmanagement.application.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = false)
public record UsersCreate (
    @NotBlank(message = "cannot be blank")
    String name,

    @NotBlank(message = "cannot be blank")
    String email,

    @NotBlank(message = "cannot be blank")
    String password,

    String title,

    @NotNull(message = "cannot be null")
    Boolean isActive,

    UUID workspaceId,

    UUID roleId
)
    {
}
