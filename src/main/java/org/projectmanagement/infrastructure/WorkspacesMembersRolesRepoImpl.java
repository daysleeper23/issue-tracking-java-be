package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WorkspacesMembersRolesRepoImpl implements WorkspacesMembersRolesRepository {
    public WorkspacesMembersRoles save(WorkspacesMembersRoles workspacesMembersRoles){
        return workspacesMembersRoles;
    };

    public Optional<WorkspacesMembersRoles> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId){
        return Optional.empty();
    };

    public Optional<WorkspacesMembersRoles> findById(UUID id) {
        return Optional.empty();
    };

    public List<WorkspacesMembersRoles> findAllByWorkspaceId(UUID workspaceId) {
        return List.of();
    };

    public WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID newRoleId){
        return new WorkspacesMembersRoles(id, UUID.randomUUID(), UUID.randomUUID(), newRoleId, Instant.now(), Instant.now());
    };
}
