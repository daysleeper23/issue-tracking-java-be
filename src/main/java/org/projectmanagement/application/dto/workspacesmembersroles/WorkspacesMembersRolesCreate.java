package org.projectmanagement.application.dto.workspacesmembersroles;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record WorkspacesMembersRolesCreate (
    @NotNull(message = "is required")
    UUID userId,

    @NotNull(message = "is required")
    UUID roleId
) {
}
