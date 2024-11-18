package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RolesJpaRepo extends JpaRepository<Roles, UUID> {
}
