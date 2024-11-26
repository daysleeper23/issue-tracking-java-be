package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "task_subscribers", uniqueConstraints = {
        @UniqueConstraint(name = "uc_tasks_subscribers",columnNames = {"taskId", "userId"})
})
public class TaskSubscribers {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(name = "id",unique = true)
    private UUID id;

    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "is_del", nullable = false)
    private boolean isDel;

    public TaskSubscribers(UUID taskId, UUID userId) {
        this.taskId = taskId;
        this.userId = userId;
    }
}
