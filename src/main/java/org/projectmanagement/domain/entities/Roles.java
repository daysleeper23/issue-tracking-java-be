package domain.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

public class Roles {
    @NonNull
    @Getter
    private final UUID id;

    @NonNull
    @Getter
    @Setter
    private String name;

    @NonNull
    @Getter
    private final Instant createdAt;

    @NonNull
    @Getter
    @Setter
    private Instant updatedAt;

    public Roles(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
