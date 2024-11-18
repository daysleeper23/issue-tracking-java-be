package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreateDTO;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsUpdateDTO;
import org.projectmanagement.domain.entities.RolesPermissions;

import java.util.List;
import java.util.UUID;

public interface RolesPermissionsService {
    RolesPermissions getOneById(UUID id);

    List<UUID> getAllPermissionsOfRoleByRoleId(UUID roleId);

    List<RolesPermissions> createRolePermissions(RolesPermissionsCreateDTO dto);

    List<RolesPermissions> addPermissionsToRole(UUID roleId, RolesPermissionsUpdateDTO dto);

    void removePermissionsFromRole(UUID roleId, RolesPermissionsUpdateDTO dto);
}
