package domain.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Getter
@Builder
public class ProjectMembers {
    private UUID id;

    @NonNull
    private final UUID userId;

    @NonNull
    private final UUID projectId;

    @NonNull
    private Boolean subscribed;

    @NonNull
    private final LocalDateTime createdAt;

    @NonNull
    private LocalDateTime updatedAt;

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setSubscribed(@NonNull Boolean subscribed) {
        this.subscribed = subscribed;
        updateTimestamp();
    }
}
