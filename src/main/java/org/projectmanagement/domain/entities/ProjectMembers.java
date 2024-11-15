package org.projectmanagement.domain.entities;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
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
    private final Instant createdAt;

    @NonNull
    private Instant updatedAt;

}
