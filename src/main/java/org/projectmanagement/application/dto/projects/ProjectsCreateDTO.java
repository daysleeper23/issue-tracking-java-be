package org.projectmanagement.application.dto.projects;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;
import java.util.UUID;


public record ProjectsCreateDTO(
        @NotBlank(message = "Can not be empty string or null")
        String name,
        String description,
        Instant endDate,
        Instant startDate,
        @Min(0)
        @Max(4)
        int priority,
        @NotNull(message = "Can not be null")
        DefaultStatus status,
        @NotNull(message = "Can not be null")
        UUID leaderId,
        @NotNull(message = "Can not be null")
        UUID workspaceId
) {

}
