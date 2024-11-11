package domain.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class Companies {

    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;

    private String description;

    private UUID ownerId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
