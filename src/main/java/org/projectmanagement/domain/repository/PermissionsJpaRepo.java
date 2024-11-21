package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionsJpaRepo extends JpaRepository<Permissions, UUID> {
    Optional<Permissions> findByName(String name);
}
