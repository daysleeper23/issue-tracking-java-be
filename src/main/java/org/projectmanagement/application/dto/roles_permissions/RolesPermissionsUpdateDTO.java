package org.projectmanagement.application.dto.roles_permissions;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record RolesPermissionsUpdateDTO(
        @NotEmpty
        List<UUID> permissions
        ) {
}
