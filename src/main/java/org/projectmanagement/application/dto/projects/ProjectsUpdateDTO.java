package org.projectmanagement.application.dto.projects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;

public record ProjectsUpdateDTO(
        String name,
        String description,
        Instant endDate,
        Instant startDate,
        @Min(0)
        @Max(4)
        int priority,
        DefaultStatus status
) {
}
