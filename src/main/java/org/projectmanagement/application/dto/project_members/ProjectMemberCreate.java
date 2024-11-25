package org.projectmanagement.application.dto.project_members;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record ProjectMemberCreate(
        @NotNull UUID userId,
        @NotNull Boolean subscribed
) {
}
