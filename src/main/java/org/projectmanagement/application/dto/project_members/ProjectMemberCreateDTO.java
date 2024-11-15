package org.projectmanagement.application.dto.project_members;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;


public record ProjectMemberCreateDTO(
        @NotNull UUID userId,
        @NotNull UUID projectId,
        @NotNull Boolean subscribed
) {
}
