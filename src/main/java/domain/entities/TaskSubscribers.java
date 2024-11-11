package domain.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Data
public class TaskSubscribers {
    @Setter(AccessLevel.NONE)
    private UUID id;
    private UUID taskId;
    private UUID userId;
}
