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
    private final InMemoryDatabase inMemoryDatabase;

    public WorkspacesMembersRolesRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public WorkspacesMembersRoles save(WorkspacesMembersRoles workspacesMembersRoles){
        return inMemoryDatabase.saveWmr(workspacesMembersRoles);
    }

    public Optional<WorkspacesMembersRoles> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId) {
        return inMemoryDatabase.wmrs.stream()
                .filter(wmr -> wmr.getUserId().equals(userId) && wmr.getWorkspaceId().equals(workspaceId))
                .findFirst();
    }

    public Optional<WorkspacesMembersRoles> findById(UUID id) {
        return inMemoryDatabase.wmrs.stream().filter(wmr -> wmr.getId().equals(id)).findFirst();
    }

    public List<WorkspacesMembersRoles> findAllByWorkspaceId(UUID workspaceId) {
        return inMemoryDatabase.wmrs.stream().filter(wmr -> wmr.getWorkspaceId().equals(workspaceId)).toList();
    }

    public WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID newRoleId) {
        return new WorkspacesMembersRoles(id, UUID.randomUUID(), UUID.randomUUID(), newRoleId, Instant.now(), Instant.now());
    }

    public void deleteById(UUID id) {
        inMemoryDatabase.wmrs.removeIf(wmr -> wmr.getId().equals(id));
    }
}
