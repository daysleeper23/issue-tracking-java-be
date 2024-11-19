package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.domain.repository.RolesPermissionsJpaRepo;
import org.projectmanagement.domain.repository.RolesPermissionsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RolesPermissionsRepoImpl implements RolesPermissionsRepository {
    private final RolesPermissionsJpaRepo rolesPermissionsJpaRepo;

    public RolesPermissionsRepoImpl(RolesPermissionsJpaRepo rolesPermissionsJpaRepo) {
        this.rolesPermissionsJpaRepo = rolesPermissionsJpaRepo;
    }

    @Override
    public RolesPermissions save(RolesPermissions rolePermission) {
        return rolesPermissionsJpaRepo.save(rolePermission);
    }

    @Override
    public Optional<RolesPermissions> findById(UUID id) {
        return rolesPermissionsJpaRepo.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        rolesPermissionsJpaRepo.deleteById(id);
    }

    @Override
    public List<UUID> findAllPermissionsOfRoleByRoleId(UUID roleId) {
        return rolesPermissionsJpaRepo.findAllPermissionsOfRoleByRoleId(roleId);
    }

    @Override
    public List<RolesPermissions> findAllRolePermissionsByRoleId(UUID roleId) {
        return rolesPermissionsJpaRepo.findAllRolePermissionsByRoleId(roleId);
    }
}
