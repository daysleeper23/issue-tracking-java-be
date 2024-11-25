package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Permissions;
import org.projectmanagement.domain.repository.PermissionsJpaRepo;
import org.projectmanagement.domain.repository.PermissionsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PermissionsRepoImpl implements PermissionsRepository {
    private final PermissionsJpaRepo permissionsJpaRepo;

    public PermissionsRepoImpl(PermissionsJpaRepo permissionsJpaRepo) {
        this.permissionsJpaRepo = permissionsJpaRepo;
    }

    @Override
    public List<UUID> findAllPermissionIds() {
        return permissionsJpaRepo.findAllPermissionIds();
    }
}
