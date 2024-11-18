package org.projectmanagement.domain.entities;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true) // here only for frameworks
@Getter
@Setter
public class RolesPermissions {
    private UUID id;

    @NotNull
    private UUID roleId;

    @NotNull
    private UUID permissionId;

}
