package domain.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

public class WorkspacesMembersRoles {
    @NonNull
    @Getter
    private final UUID id;

    @NonNull
    @Getter
    private final UUID workspaceId;

    @NonNull
    @Getter
    private final UUID userId;

    @NonNull
    @Getter
    @Setter
    private UUID roleId;

    public WorkspacesMembersRoles(UUID id, UUID workspaceId, UUID userId, UUID roleId) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.roleId = roleId;
    }
}
