package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity(name = "task_subscribers")
public class TaskSubscribers {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id",unique = true)
    private UUID id;

    private UUID taskId;

    private UUID userId;

    public TaskSubscribers(UUID taskId, UUID userId) {
        this.taskId = taskId;
        this.userId = userId;
    }
}
