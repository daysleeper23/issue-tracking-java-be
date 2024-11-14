package org.projectmanagement.domain.entities;

import lombok.*;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
public class Projects {
    private final UUID id;

    @NonNull
    private String name;

    private String description;

    private Instant endDate;

    private Instant startDate;

    private int priority;

    @NonNull
    private DefaultStatus status;

    @NonNull
    private UUID leaderId;

    @NonNull
    private UUID workspaceId;

    //@NonNull

    private Instant createdAt;

    //@NonNull
    private Instant updatedAt;

}

