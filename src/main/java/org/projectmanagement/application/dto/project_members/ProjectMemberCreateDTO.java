package org.projectmanagement.application.dto.project_members;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class ProjectMemberCreateDTO {
    @NonNull
    private final UUID userId;

    @NotNull
    private final UUID projectId;

    @NotNull
    private Boolean subscribed;

}
