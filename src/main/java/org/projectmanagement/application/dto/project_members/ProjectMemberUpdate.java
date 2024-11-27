package org.projectmanagement.application.dto.project_members;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ProjectMemberUpdate(
        @NotNull
        Boolean subscribed
) {
}
