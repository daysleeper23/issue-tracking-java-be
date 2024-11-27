package org.projectmanagement.application.dto.roles_permissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = false)
public record RolesPermissionsUpdate(
        @NotEmpty
        List<UUID> permissions
        ) {
}
