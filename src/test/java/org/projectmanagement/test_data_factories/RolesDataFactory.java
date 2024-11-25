package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.domain.repository.PermissionsRepoJpa;
import org.projectmanagement.domain.repository.RolesPermissionsRepoJpa;
import org.projectmanagement.domain.repository.RolesRepoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RolesDataFactory {
    @Autowired
    RolesRepoJpa rolesRepoJpa;

    @Autowired
    RolesPermissionsRepoJpa rolesPermissionsRepoJpa;

    @Autowired
    PermissionsRepoJpa permissionsRepoJpa;


    public void deleteAll() {
        rolesRepoJpa.deleteAll();
        rolesPermissionsRepoJpa.deleteAll();
    }

    public UUID createRoleWithAllPermissions(String roleName, UUID companyId) {
        Roles role = rolesRepoJpa.save(Roles.builder()
                .name(roleName)
                .companyId(companyId)
                .isDeleted(false)
                .isSystemRole(false)
                .build());
        List<UUID> permissions = permissionsRepoJpa.findAllPermissionIds();

        for (UUID permissionId : permissions) {
            rolesPermissionsRepoJpa.save(RolesPermissions.builder()
                    .permissionId(permissionId)
                    .roleId(role.getId())
                    .build());
        }

        return role.getId();
    }


}
