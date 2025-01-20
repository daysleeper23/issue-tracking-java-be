package org.projectmanagement.application.dto.roles_permissions;

import java.util.UUID;

public record RolesPermissionsRead(
    UUID id,
    UUID roleId,
    UUID permissionId,
    String name
) {
}
