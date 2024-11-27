package org.projectmanagement.application.dto.workspaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = false)
public record WorkspacesCreate (
    @NotBlank(message = "must not be empty or whitespace")
    String name,

    String description
) {
}
