package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesJpaRepo;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkspacesMembersRolesJpaRepoImpl implements WorkspacesMembersRolesRepository {
    private final WorkspacesMembersRolesJpaRepo workspacesMembersRolesJpaRepo;

    public WorkspacesMembersRolesJpaRepoImpl(WorkspacesMembersRolesJpaRepo workspacesMembersRolesJpaRepo) {
        this.workspacesMembersRolesJpaRepo = workspacesMembersRolesJpaRepo;
    }

    @Override
    public WorkspacesMembersRoles save(WorkspacesMembersRoles workspacesMembersRoles) {
        return workspacesMembersRolesJpaRepo.save(workspacesMembersRoles);
    }

    @Override
    public Optional<WorkspacesMembersRoles> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId) {
        return Optional.empty();
    }

    @Override
    public Optional<WorkspacesMembersRoles> findById(UUID id) {
        return workspacesMembersRolesJpaRepo.findById(id);
    }

    @Override
    public List<WorkspacesMembersRoles> findAllByWorkspaceId(UUID workspaceId) {
        return List.of();
    }

    @Override
    public WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID newRoleId) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }
}
