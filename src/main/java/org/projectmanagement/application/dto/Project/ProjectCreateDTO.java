package org.projectmanagement.application.dto.Project;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @NonNull
    private String name;

    private String description;

    private Instant endDate;

    private Instant startDate;

    @Min(0)
    @Max(4)
    private short priority;

    @NonNull
    private DefaultStatus status;

    @NonNull
    private UUID leaderId;

    @NonNull
    private final UUID workspaceId;
}
