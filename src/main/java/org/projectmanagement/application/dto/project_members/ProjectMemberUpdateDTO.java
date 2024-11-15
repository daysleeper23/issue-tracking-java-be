package org.projectmanagement.application.dto.project_members;

import lombok.*;

public record ProjectMemberUpdateDTO (
        @NonNull
        Boolean subscribed
) {

}
