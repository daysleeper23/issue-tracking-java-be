package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.domain.repository.RolesPermissionsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RolesPermissionsRepoImpl implements RolesPermissionsRepository {
    private final InMemoryDatabase inMemoryDatabase;

    public RolesPermissionsRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    @Override
    public RolesPermissions save(RolesPermissions rolePermission) {
        return null;
    }

    @Override
    public Optional<RolesPermissions> findById(UUID id) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {
    }

    @Override
    public List<UUID> findAllPermissionsOfRoleByRoleId(UUID roleId) {
        return List.of();
    }

    @Override
    public List<RolesPermissions> findAllRolePermissionsByRoleId(UUID roleId) {
        return List.of();
    }
}
