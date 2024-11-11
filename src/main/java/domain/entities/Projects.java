package domain.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@Builder
public class Projects {
    private final UUID id;

    @NonNull
    private String name;

    private String description;

    private LocalDateTime endDate;

    private LocalDateTime startDate;

    private short priority;

    @NonNull
    private ProjectStatus status;

    @NonNull
    private UUID leaderId;

    @NonNull
    private final UUID workspaceId;

    @NonNull
    private final LocalDateTime createdAt;

    @NonNull
    private LocalDateTime updatedAt;

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setName(@NonNull String name) {
        this.name = name;
        updateTimestamp();
    }

    public void setStatus(@NonNull ProjectStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
        updateTimestamp();
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        updateTimestamp();
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        updateTimestamp();
    }

    public void setPriority(short priority) {
        this.priority = priority;
        updateTimestamp();
    }
}

