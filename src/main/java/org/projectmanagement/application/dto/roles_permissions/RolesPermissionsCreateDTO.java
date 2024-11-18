package org.projectmanagement.application.dto.roles_permissions;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true) // only for frameworks
@AllArgsConstructor
@Builder
public class RolesPermissionsCreateDTO {
    @NotNull
    private UUID roleId;

    @NotNull
    private UUID permissionId;
}
