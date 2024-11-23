package org.projectmanagement.domain.repository;


import org.projectmanagement.domain.entities.RolesPermissions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolesPermissionsRepository {
    RolesPermissions save(RolesPermissions rolePermission);

    Optional<RolesPermissions> findById(UUID id);

    void deleteById(UUID id);

    List<UUID> findAllPermissionsOfRoleByRoleId(UUID roleId);

    List<RolesPermissions> findAllRolePermissionsByRoleId(UUID roleId);

    List<RolesPermissions> findAllRolesPermissionsForCompany(UUID companyId);

}
