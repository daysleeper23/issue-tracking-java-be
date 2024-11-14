package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;

import java.util.UUID;

public interface WorkspacesMembersRolesService {
    public WorkspacesMembersRoles createWorkspacesMembersRoles(WorkspacesMembersRolesCreate workspacesMembersRoles);

    public WorkspacesMembersRoles getWorkspacesMembersRolesForUser(UUID userId, UUID workspaceId);

    public WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, WorkspacesMembersRolesCreate newRole);
}
