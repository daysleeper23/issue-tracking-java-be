package org.projectmanagement.application.dto.workspaces;

import jakarta.validation.constraints.NotBlank;

public record WorkspacesCreate (
    @NotBlank(message = "must not be empty or whitespace")
    String name,

    String description
) {
}
