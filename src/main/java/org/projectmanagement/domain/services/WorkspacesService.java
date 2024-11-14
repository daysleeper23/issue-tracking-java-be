package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.workspaces.WorkspacesCreate;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesService {
    WorkspacesRead createWorkspace(WorkspacesCreate workspace);

    WorkspacesRead findById(UUID id);

    List<WorkspacesRead> findAllWorkspacesOfCompany(UUID companyId);

    WorkspacesRead updateWorkspace(UUID id, WorkspacesCreate workspace);

    void deleteWorkspace(UUID id);
}
