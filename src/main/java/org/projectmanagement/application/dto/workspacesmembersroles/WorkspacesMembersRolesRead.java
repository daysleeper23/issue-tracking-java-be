package org.projectmanagement.application.dto.workspacesmembersroles;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class WorkspacesMembersRolesRead {
    private UUID id;
    private UUID userId;
    private UUID workspaceId;
    private UUID roleId;
    private Instant updatedAt;
}
