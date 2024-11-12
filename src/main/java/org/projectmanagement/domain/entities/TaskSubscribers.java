package org.projectmanagement.domain.entities;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskSubscribers {
    @Setter(AccessLevel.NONE)
    private UUID id;
    private UUID taskId;
    private UUID userId;
}
