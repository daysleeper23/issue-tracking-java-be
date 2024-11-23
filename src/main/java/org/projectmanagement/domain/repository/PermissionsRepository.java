package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Permissions;

import java.util.List;
import java.util.UUID;

public interface PermissionsRepository {
    List<UUID> findAllPermissionIds();
}
