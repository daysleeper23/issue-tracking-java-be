package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.repository.PermissionsRepoJpa;
import org.projectmanagement.domain.repository.PermissionsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PermissionsRepoImpl implements PermissionsRepository {
    private final PermissionsRepoJpa permissionsRepoJpa;

    public PermissionsRepoImpl(PermissionsRepoJpa permissionsRepoJpa) {
        this.permissionsRepoJpa = permissionsRepoJpa;
    }

    @Override
    public List<UUID> findAllPermissionIds() {
        return permissionsRepoJpa.findAllPermissionIds();
    }
}
