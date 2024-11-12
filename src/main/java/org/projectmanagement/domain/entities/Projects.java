package org.projectmanagement.domain.entities;

import lombok.*;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@Builder
public class Projects {
    private final UUID id;

    @NonNull
    private String name;

    private String description;

    private Instant endDate;

    private Instant startDate;

    private short priority;

    @NonNull
    private DefaultStatus status;

    @NonNull
    private UUID leaderId;

    @NonNull
    private final UUID workspaceId;

    @NonNull
    private final Instant createdAt;

    @NonNull
    private Instant updatedAt;

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public void setName(@NonNull String name) {
        this.name = name;
        updateTimestamp();
    }

    public void setStatus(@NonNull DefaultStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
        updateTimestamp();
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
        updateTimestamp();
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
        updateTimestamp();
    }

    public void setPriority(short priority) {
        this.priority = priority;
        updateTimestamp();
    }
}

