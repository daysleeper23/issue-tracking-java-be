package domain.entities;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Getter
@Builder
public class RolesPermissions {
    private UUID id;

    @NonNull
    private UUID roleId;

    @NonNull
    private UUID permissionId;

    @NonNull
    private final LocalDateTime createdAt;

    @NonNull
    private LocalDateTime updatedAt;

    /*
    IF no setters do we need updatedAt?
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    */
}
