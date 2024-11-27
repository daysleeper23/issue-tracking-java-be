package org.projectmanagement.application.dto.workspacesmembersroles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = false)
public record WorkspacesMembersRolesCreate (
    @NotNull(message = "is required")
    UUID userId,

    @NotNull(message = "is required")
    UUID roleId
) {
}
