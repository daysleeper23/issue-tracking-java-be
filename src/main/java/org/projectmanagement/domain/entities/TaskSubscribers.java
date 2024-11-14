package org.projectmanagement.domain.entities;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@Data
public class TaskSubscribers {
    @Setter(AccessLevel.NONE)
    private UUID id;
    private UUID taskId;
    private UUID userId;

    public TaskSubscribers(UUID taskId, UUID userId){
        this.userId = userId;
        this.taskId = taskId;
    }
}
