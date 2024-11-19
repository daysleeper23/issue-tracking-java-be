package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.workspaces.WorkspacesCreate;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.projectmanagement.application.dto.workspaces.WorkspacesUpdate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesService {
    Optional<WorkspacesRead> createWorkspace(UUID companyId, WorkspacesCreate workspace);

    Optional<WorkspacesRead> findById(UUID id);

    List<WorkspacesRead> findAllWorkspaces(UUID companyId);

    Optional<WorkspacesRead> updateWorkspace(UUID id, WorkspacesUpdate workspace);

    void deleteWorkspace(UUID id);
}
