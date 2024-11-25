package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.domain.repository.RolesPermissionsRepoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RolesPermissionsDataFactory {

    @Autowired
    RolesPermissionsRepoJpa rolesPermissionsRepoJpa;

    public UUID addPermissionToRole(UUID roleId, UUID permissionId) {
        RolesPermissions rolesPermission = rolesPermissionsRepoJpa.save(RolesPermissions.builder()
               .roleId(roleId)
               .permissionId(permissionId)
               .build());
        return rolesPermission.getId();
    }
}
