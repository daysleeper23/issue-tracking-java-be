package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WorkspaceMemberRolesDataFactory {
    @Autowired
    private WorkspacesMembersRolesRepoJpa workspacesMembersRolesRepoJpa;

    public void deleteAll() {
        workspacesMembersRolesRepoJpa.deleteAll();
    }

    public UUID createWorkspacesMembersRole(UUID userId, UUID roleId, UUID workspaceId) {
        WorkspacesMembersRoles workspacesMembersRole = workspacesMembersRolesRepoJpa.save(WorkspacesMembersRoles.builder()
                .userId(userId)
                .roleId(roleId)
                .workspaceId(workspaceId)
                .build());

        return workspacesMembersRole.getId();
    }
}
