package org.projectmanagement.application.dto.tasks;

import lombok.*;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TaskInfo {
    private UUID id;
    private String name;
    private String description;
    private DefaultStatus status;
    private UUID assigneeId;
    private short priority;
    private UUID projectId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant startedAt;
    private Instant endedAt;
    private List<TaskSubscribers> subscribers;
}
