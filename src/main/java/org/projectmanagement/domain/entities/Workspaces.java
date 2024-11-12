package org.projectmanagement.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

public class Workspaces {
    @NonNull
    @Getter
    private final UUID id;

    @NonNull
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @NonNull
    @Getter
    @Setter
    private UUID companyId;

    @NonNull
    @Getter
    private final Instant createdAt;

    @NonNull
    @Getter
    @Setter
    private Instant updatedAt;

    public Workspaces(UUID id, String name, String description, UUID companyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.companyId = companyId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
