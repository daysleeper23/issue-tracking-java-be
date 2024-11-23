package org.projectmanagement.application.dto.roles_permissions;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record RolesPermissionsCreate(
    @NotNull
    String roleName,

    @NotEmpty
    List<UUID> permissions
) {
}
