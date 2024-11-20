package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreate;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsUpdate;
import org.projectmanagement.domain.entities.RolesPermissions;

import java.util.List;
import java.util.UUID;

public interface RolesPermissionsService {
    RolesPermissions getOneById(UUID id);

    List<UUID> getAllPermissionsOfRoleByRoleId(UUID roleId);

    List<RolesPermissions> createRolePermissions(RolesPermissionsCreate dto);

    List<RolesPermissions> addPermissionsToRole(UUID roleId, RolesPermissionsUpdate dto);

    void removePermissionsFromRole(UUID roleId, RolesPermissionsUpdate dto);
}
