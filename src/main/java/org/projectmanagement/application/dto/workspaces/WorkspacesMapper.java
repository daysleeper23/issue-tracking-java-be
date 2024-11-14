package org.projectmanagement.application.dto.workspaces;

import org.projectmanagement.application.dto.users.UsersRead;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.entities.Workspaces;

public class WorkspacesMapper {
    public static WorkspacesRead toWorkspacesRead(Workspaces workspaces) {
        return new WorkspacesRead(
                workspaces.getId(),
                workspaces.getName(),
                workspaces.getDescription(),
                workspaces.getCompanyId(),
                workspaces.getCreatedAt(),
                workspaces.getUpdatedAt());
    }
}
