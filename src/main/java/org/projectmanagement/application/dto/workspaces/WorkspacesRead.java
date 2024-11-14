package org.projectmanagement.application.dto.workspaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class WorkspacesRead {
    private UUID id;
    private String name;
    private String description;
    private UUID companyId;
    private Instant createdAt;
    private Instant updatedAt;
}
