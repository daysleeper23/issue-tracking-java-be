package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesMembersRolesRepository extends JpaRepository<WorkspacesMembersRoles, UUID> {
//    WorkspacesMembersRoles save(WorkspacesMembersRoles workspacesMembersRoles);

    @Query("SELECT w FROM WorkspacesMembersRoles w WHERE w.userId = :userId AND w.workspaceId = :workspaceId")
    Optional<WorkspacesMembersRoles> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

//    Optional<WorkspacesMembersRoles> findById(UUID id);

    @Query("SELECT w FROM WorkspacesMembersRoles w WHERE w.workspaceId = :workspaceId")
    List<WorkspacesMembersRoles> findAllByWorkspaceId(UUID workspaceId);

    @Query("UPDATE WorkspacesMembersRoles w SET w.roleId = :newRoleId WHERE w.id = :id")
    WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID newRoleId);

    @Query("DELETE FROM WorkspacesMembersRoles w WHERE w.id = :id")
    void deleteById(UUID id);
}
