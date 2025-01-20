package org.projectmanagement.application.dto.tasks;

import lombok.*;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record TasksInfo(UUID id,
                         String title,
                         String description,
                         DefaultStatus status,
                         UUID assigneeId,
                         UUID workspaceId,
                         short priority,
                         UUID projectId,
                         Instant createdAt,
                         Instant updatedAt,
                         Instant startedAt,
                         Instant endedAt,
                         List<TaskSubscribers> subscribers) {
}
