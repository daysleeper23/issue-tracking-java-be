package org.projectmanagement.application.dto.tasks;


import lombok.Builder;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.util.UUID;

@Builder
public record TasksCompact(String id,String name,
                           DefaultStatus status,
                           UUID projectId,
                           String assigneeId,
                           short priority) {
}
