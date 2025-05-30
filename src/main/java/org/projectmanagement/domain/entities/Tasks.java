package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.exceptions.InvalidInputException;
import lombok.*;
import org.apache.commons.lang3.EnumUtils;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "tasks", uniqueConstraints = {
        @UniqueConstraint(name = "uc_tasks_name",columnNames = {"name", "project_id"})
})
public class Tasks extends BaseEntity {

    //Todo: Uncomment the line below after implementing jpa
//    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",unique = true)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    //Todo: Redundant annotation, remove it after implementing jpa
    @Builder.Default
    private DefaultStatus status= DefaultStatus.BACKLOG;

    @Column(nullable = false, name = "assignee_id")
    private UUID assigneeId;

    @Column(nullable = false, name = "priority")
    private short priority;

    @Column(nullable = false, name = "project_id")
    private UUID projectId;

    @Column(nullable = false, name = "workspace_id")
    private UUID workspaceId;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    public abstract static class TasksBuilder<C extends Tasks, B extends Tasks.TasksBuilder<C, B>>
            extends BaseEntity.BaseEntityBuilder<C, B> {

        public TasksBuilder startedAt(Instant startedAt) {
            if (startedAt != null && this.endedAt != null && startedAt.isAfter(this.endedAt)) {
                throw new InvalidInputException("Started at cannot be after ended at");
            }
            this.startedAt = startedAt;
            return this;
        }

        public TasksBuilder endedAt(Instant endedAt) {
            if (endedAt != null && this.startedAt != null && endedAt.isBefore(this.startedAt)) {
                throw new InvalidInputException("Ended at cannot be before started at");
            }
            this.endedAt = endedAt;
            return this;
        }

    }

}
