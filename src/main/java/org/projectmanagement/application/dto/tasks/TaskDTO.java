package org.projectmanagement.application.dto.tasks;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record TaskDTO(@NotBlank(message = "Name length must greater than 0") String name,
                      String description,
                      @NotNull String projectId,
                      @Min(value = 0, message = "Priority must be greater than 0")
                      @Max(value = 4, message = "Priority must be lesser than 4")
                      short priority,
                      String status,
                      String assigneeId,
                      Instant startedAt,
                      Instant endedAt) {
}
