package org.projectmanagement.application.dto.roles_permissions;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record RolesPermissionsCreateDTO(
    @NotNull
    UUID roleId,
    @NotEmpty
    List<UUID> permissions
) {
}
