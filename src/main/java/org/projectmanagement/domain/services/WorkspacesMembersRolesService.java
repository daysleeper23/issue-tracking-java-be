package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesRead;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;

import java.util.List;
import java.util.UUID;

public interface WorkspacesMembersRolesService {
    WorkspacesMembersRolesRead createMembersRolesForWorkspace(WorkspacesMembersRolesCreate wmrc);

    WorkspacesMembersRolesRead getWorkspacesMembersRolesForUser(UUID userId, UUID workspaceId);

    List<WorkspacesMembersRolesRead> getMembersRolesForWorkspace(UUID workspaceId);

    WorkspacesMembersRolesRead updateWorkspacesMembersRoles(UUID id, WorkspacesMembersRolesCreate newRole);

    void deleteWorkspacesMembersRoles(UUID id);
}
