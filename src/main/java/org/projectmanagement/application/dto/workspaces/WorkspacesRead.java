package org.projectmanagement.application.dto.workspaces;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
public class WorkspacesRead {
    private UUID id;
    private String name;
    private String description;
    private UUID companyId;
    private Instant createdAt;
    private Instant updatedAt;
}
