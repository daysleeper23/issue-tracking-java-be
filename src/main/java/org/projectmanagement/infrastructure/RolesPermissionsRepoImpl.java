package org.projectmanagement.infrastructure;

import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsRead;
import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.domain.repository.RolesPermissionsRepoJpa;
import org.projectmanagement.domain.repository.RolesPermissionsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RolesPermissionsRepoImpl implements RolesPermissionsRepository {
    private final RolesPermissionsRepoJpa rolesPermissionsRepoJpa;

    public RolesPermissionsRepoImpl(RolesPermissionsRepoJpa rolesPermissionsRepoJpa) {
        this.rolesPermissionsRepoJpa = rolesPermissionsRepoJpa;
    }

    @Override
    public RolesPermissions save(RolesPermissions rolePermission) {
        return rolesPermissionsRepoJpa.save(rolePermission);
    }

    @Override
    public Optional<RolesPermissions> findById(UUID id) {
        return rolesPermissionsRepoJpa.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        rolesPermissionsRepoJpa.deleteById(id);
    }

    //@Cacheable(value = "rolesPermissions", key = "#roleId")
    @Override
    public List<UUID> findAllPermissionsOfRoleByRoleId(UUID roleId) {
        return rolesPermissionsRepoJpa.findAllPermissionsOfRoleByRoleId(roleId);
    }

    @Override
    public List<RolesPermissions> findAllRolePermissionsByRoleId(UUID roleId) {
        return rolesPermissionsRepoJpa.findAllRolePermissionsByRoleId(roleId);
    }

    @Override
    public List<RolesPermissionsRead> findAllRolesPermissionsForCompany(UUID companyId) {
        return rolesPermissionsRepoJpa.findAllByCompanyId(companyId);
    }
}
