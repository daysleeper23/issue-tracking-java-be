package org.projectmanagement.domain.entities;

import lombok.*;

import java.time.Instant;
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
    private final Instant createdAt;

    @NonNull
    private Instant updatedAt;

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public void setRoleId(@NonNull UUID roleId) {
        //has to be updated later to implement logic below
        //admin or company managers roles allowed
        this.roleId = roleId;
        updateTimestamp();
    }
}
