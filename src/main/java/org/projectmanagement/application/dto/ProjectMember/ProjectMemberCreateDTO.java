package org.projectmanagement.application.dto.ProjectMember;

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
    @NotNull
    private final UUID userId;

    @NotNull
    @NonNull
    private final UUID projectId;

    @NotNull
    @NonNull
    private Boolean subscribed;

}
