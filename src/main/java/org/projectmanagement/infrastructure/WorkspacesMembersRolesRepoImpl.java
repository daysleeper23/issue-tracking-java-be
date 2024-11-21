package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepoJpa;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WorkspacesMembersRolesRepoImpl implements WorkspacesMembersRolesRepository {
    private final WorkspacesMembersRolesRepoJpa jpaRepo;

    public WorkspacesMembersRolesRepoImpl(WorkspacesMembersRolesRepoJpa workspacesMembersRolesRepoJpa) {
        this.jpaRepo = workspacesMembersRolesRepoJpa;
    }

    public WorkspacesMembersRoles save(WorkspacesMembersRoles workspacesMembersRoles) {
        return jpaRepo.save(workspacesMembersRoles);
    }

    public Optional<WorkspacesMembersRoles> findByUserId(UUID userId) {
        return jpaRepo.findByUserId(userId);
    }

    public Optional<WorkspacesMembersRoles> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId) {
        return jpaRepo.findByUserIdAndWorkspaceId(userId, workspaceId);
    }

    public Optional<WorkspacesMembersRoles> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    public List<WorkspacesMembersRoles> findAllByWorkspaceId(UUID workspaceId) {
        return jpaRepo.findAllByWorkspaceId(workspaceId);
    }

    public WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID newRoleId) {
        return jpaRepo.updateWorkspacesMembersRoles(id, newRoleId);
    }

    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }
}
