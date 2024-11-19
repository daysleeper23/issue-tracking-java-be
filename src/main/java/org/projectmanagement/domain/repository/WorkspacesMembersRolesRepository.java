package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesMembersRolesRepository {
    WorkspacesMembersRoles save(WorkspacesMembersRoles workspacesMembersRoles);
    
    Optional<WorkspacesMembersRoles> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

    List<WorkspacesMembersRoles> findAllByWorkspaceId(UUID workspaceId);

    WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID newRoleId);

    void deleteById(UUID id);

    Optional<WorkspacesMembersRoles> findById(UUID id);
}
