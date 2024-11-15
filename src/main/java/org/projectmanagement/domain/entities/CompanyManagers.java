package org.projectmanagement.domain.entities;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
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

}
