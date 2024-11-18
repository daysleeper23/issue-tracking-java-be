package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkspacesMembersRolesJpaRepo extends JpaRepository<WorkspacesMembersRoles, UUID> {
}
