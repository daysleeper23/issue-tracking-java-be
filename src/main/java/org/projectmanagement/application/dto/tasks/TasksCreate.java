package org.projectmanagement.application.dto.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.projectmanagement.application.utils.validation.annotation.EnumValidation;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;

@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public record TasksCreate(
        @NotBlank(message = "length must greater than 0")
        String title,
        String description,
        @UUID(message = "must be a valid UUID")
        String projectId,
        @Min(value = 0, message = "must be greater than 0")
        @Max(value = 4, message = "must be lesser than 4")
        short priority,
        @EnumValidation(target = DefaultStatus.class)
        String status,
        @UUID(message = "must be a valid UUID")
        String workspaceId,
        @UUID(message = "must be a valid UUID")
        String assigneeId,
        Instant startedAt,
        Instant endedAt
) {
}
