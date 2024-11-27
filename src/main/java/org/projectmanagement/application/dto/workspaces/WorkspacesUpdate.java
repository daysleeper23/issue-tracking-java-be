package org.projectmanagement.application.dto.workspaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = false)
public record WorkspacesUpdate(
    // Allows null but enforces non-empty if not null
    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String name,
    String description
) {
    public static WorkspacesUpdate withName(String name) {
        return new WorkspacesUpdate(name, null);
    }
}
