package org.projectmanagement.domain.entities;

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
@Builder(builderClassName = "TasksBuilder",toBuilder = true)
public class Tasks {

    //Todo: Uncomment the line below after implementing jpa
//    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;

    private String description;

    //Todo: Redundant annotation, remove it after implementing jpa
    @Builder.Default
    private DefaultStatus status= DefaultStatus.TODO;

    private UUID assigneeId;

    private short priority;

    private UUID projectId;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant updatedAt = Instant.now();;

    private Instant startedAt;

    private Instant endedAt;

    public static class TasksBuilder{

        public TasksBuilder priority(short priority){
            if (priority < 0 || priority > 4){
                throw new InvalidInputException("Illegal priority value");
            }
            this.priority = priority;
            return this;
        }

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
