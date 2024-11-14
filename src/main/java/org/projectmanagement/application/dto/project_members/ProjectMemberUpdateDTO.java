package org.projectmanagement.application.dto.project_members;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ProjectMemberUpdateDTO {
    @NonNull
    private Boolean subscribed;
}
