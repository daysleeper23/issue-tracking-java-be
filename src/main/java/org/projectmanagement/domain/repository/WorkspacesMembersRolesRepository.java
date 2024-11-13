package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesMembersRolesRepository {
    WorkspacesMembersRoles save(WorkspacesMembersRoles workspacesMembersRoles);

    Optional<WorkspacesMembersRoles> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

    Optional<WorkspacesMembersRoles> findById(UUID id);

    List<WorkspacesMembersRoles> findAllByWorkspaceId(UUID workspaceId);

    WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID newRoleId);
}
