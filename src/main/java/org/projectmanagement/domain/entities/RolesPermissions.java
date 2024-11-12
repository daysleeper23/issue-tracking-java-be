package org.projectmanagement.domain.entities;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Builder
public class RolesPermissions {
    private UUID id;

    @NotNull
    private UUID roleId;

    @NotNull
    private UUID permissionId;

    @NotNull
    private final Instant createdAt;
}
