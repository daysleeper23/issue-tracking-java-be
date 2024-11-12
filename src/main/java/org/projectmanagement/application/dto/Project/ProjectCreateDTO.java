package org.projectmanagement.application.dto.Project;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class ProjectCreateDTO {
    @NotNull
    @NonNull
    private String name;

    private String description;

    private Instant endDate;

    private Instant startDate;

    private short priority;

    @NotNull
    @NonNull
    private DefaultStatus status;

    @NotNull
    @NonNull
    private UUID leaderId;

    @NotNull
    @NonNull
    private final UUID workspaceId;
}
