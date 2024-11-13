package org.projectmanagement.application.dto.workspacesmembersroles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@NonNull
@Getter
@Setter
@AllArgsConstructor
public class WorkspacesMembersRolesCreate {
    private UUID userId;
    private UUID workspaceId;
    private UUID roleId;
}
