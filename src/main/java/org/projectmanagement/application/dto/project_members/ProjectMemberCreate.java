package org.projectmanagement.application.dto.project_members;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ProjectMemberCreate(
        @NotNull UUID userId,
        @NotNull Boolean subscribed
) {
}
