package org.projectmanagement.domain.entities;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
public class Companies {

    @Setter(AccessLevel.NONE)
    private UUID id;
    @NonNull
    private String name;

    private String description;

    @NonNull
    private UUID ownerId;

    private Instant createdAt;

    private Instant updatedAt;

    public Companies(@NonNull String name, @NonNull UUID userId){
        this.name = name;
        this.ownerId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
