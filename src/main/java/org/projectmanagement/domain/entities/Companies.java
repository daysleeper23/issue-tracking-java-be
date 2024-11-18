package org.projectmanagement.domain.entities;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
public class Companies {

    //TODO: Uncomment after integrate with db
//    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;

    private String description;

    private UUID ownerId;

    private Instant createdAt;

    private Instant updatedAt;

    public Companies(String name,String description ,UUID userId){
        this.name = name;
        this.description = description;
        this.ownerId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Companies(UUID id, @NonNull String name, String description, @NonNull UUID ownerId, Instant createdAt, Instant updatedAt){
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
