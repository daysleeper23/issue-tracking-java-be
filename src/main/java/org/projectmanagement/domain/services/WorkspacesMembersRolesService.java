package org.projectmanagement.domain.services;

import org.projectmanagement.domain.entities.WorkspacesMembersRoles;

import java.util.UUID;

public interface WorkspacesMembersRolesService {
    public WorkspacesMembersRoles createWorkspacesMembersRoles(WorkspacesMembersRoles workspacesMembersRoles);

    public WorkspacesMembersRoles getWorkspacesMembersRolesForUser(UUID userId, UUID workspaceId);

    public WorkspacesMembersRoles updateWorkspacesMembersRoles(UUID id, UUID userId, UUID workspaceId, UUID newRoleId);
}
