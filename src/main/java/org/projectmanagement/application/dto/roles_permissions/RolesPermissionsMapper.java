package org.projectmanagement.application.dto.roles_permissions;

import org.projectmanagement.domain.entities.RolesPermissions;

public class RolesPermissionsMapper {
    public static RolesPermissions CreateDTOToRolesPermissions(RolesPermissionsCreateDTO dto) {
        return new RolesPermissions(dto.getRoleId(), dto.getPermissionId());
    }
}
