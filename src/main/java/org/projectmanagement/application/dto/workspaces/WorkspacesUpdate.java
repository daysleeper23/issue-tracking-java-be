package org.projectmanagement.application.dto.workspaces;

import jakarta.validation.constraints.Pattern;

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
