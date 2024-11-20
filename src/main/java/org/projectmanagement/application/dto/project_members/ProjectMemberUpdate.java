package org.projectmanagement.application.dto.project_members;

import lombok.*;

public record ProjectMemberUpdate(
        @NonNull
        Boolean subscribed
) {

}
