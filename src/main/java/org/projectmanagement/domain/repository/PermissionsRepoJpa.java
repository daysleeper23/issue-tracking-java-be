package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionsRepoJpa extends JpaRepository<Permissions, UUID> {
    Optional<Permissions> findByName(String name);

    @Query(value = "SELECT id " +
            "FROM permissions ",
            nativeQuery = true)
    List<UUID> findAllPermissionIds();
}
