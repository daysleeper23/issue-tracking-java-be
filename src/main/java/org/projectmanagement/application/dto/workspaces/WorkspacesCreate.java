package org.projectmanagement.application.dto.workspaces;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class WorkspacesCreate {
    @NotBlank(message = "is required")
    private String name;

    private String description;

    @NotNull(message = "is required")
    private UUID companyId;
}
