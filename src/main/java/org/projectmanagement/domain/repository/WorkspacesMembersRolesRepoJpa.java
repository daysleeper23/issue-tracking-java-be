package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspacesMembersRolesRepoJpa extends JpaRepository<WorkspacesMembersRoles, UUID> {
    @Query(value = "SELECT * " +
            "FROM workspaces_members_roles w " +
            "WHERE w.workspace_id = :workspaceId"
        , nativeQuery = true)
    List<WorkspacesMembersRoles> findAllByWorkspaceId(UUID workspaceId);

    @Query(value = "SELECT * " +
            "FROM workspaces_members_roles w " +
            "WHERE w.user_id = :userId " +
            "AND w.workspace_id = :workspaceId"
        , nativeQuery = true)
    Optional<WorkspacesMembersRoles> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

    @Modifying
    @Query(value = "UPDATE workspaces_members_roles w " +
            "SET w.role_id = :newRoleId " +
            "WHERE w.id = :id"
        , nativeQuery = true)
    WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID newRoleId);
}
