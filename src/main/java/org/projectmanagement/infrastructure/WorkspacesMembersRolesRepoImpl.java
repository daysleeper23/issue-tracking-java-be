package org.projectmanagement.infrastructure;

import jakarta.transaction.Transactional;
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

    public Optional<WorkspacesMembersRoles> updateWorkspacesMembersRoles(UUID id, UUID newRoleId) {
        int result = jpaRepo.updateWorkspacesMembersRoles(id, newRoleId);
        if (result == 1) {
            Optional<WorkspacesMembersRoles> newWMR = jpaRepo.findById(id);
            newWMR.get().setRoleId(newRoleId);
            return newWMR;
        }
        return Optional.empty();
    }

    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }
}
