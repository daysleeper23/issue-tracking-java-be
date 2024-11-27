package org.projectmanagement.application.dto.projects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectmanagement.application.utils.validation.annotation.EnumValidation;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;
import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = false)
public record ProjectsCreate(
        @NotBlank(message = "Can not be empty string or null")
        String name,
        String description,
        Instant endDate,
        Instant startDate,
        @Min(0)
        @Max(4)
        Integer priority,
        @EnumValidation(target = DefaultStatus.class)
        DefaultStatus status,
        UUID leaderId,
        @NotNull(message = "Can not be null")
        UUID workspaceId
) {

}
