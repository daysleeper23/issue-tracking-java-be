package domain.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Getter
@Builder
public class CompanyManagers {
    private UUID id;

    @NonNull
    private UUID userId;

    @NonNull
    private UUID companyId;

    @NonNull
    private UUID roleId;

    @NonNull
    private final LocalDateTime createdAt;

    @NonNull
    private LocalDateTime updatedAt;

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setRoleId(@NonNull UUID roleId) {
        //has to be updated later to implement logic below
        //admin or company managers roles allowed
        this.roleId = roleId;
        updateTimestamp();
    }
}
