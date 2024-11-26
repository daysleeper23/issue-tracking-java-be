package org.projectmanagement.application.dto.project_members;

import jakarta.validation.constraints.NotNull;

public record ProjectMemberUpdate(
        @NotNull
        Boolean subscribed
) {
}
