package org.projectmanagement.application.dto.ProjectMember;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ProjectMemberUpdateDTO {

    @NotNull
    @NonNull
    private Boolean subscribed;
}
