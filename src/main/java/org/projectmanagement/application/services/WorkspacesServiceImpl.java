package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.workspaces.WorkspacesCreate;
import org.projectmanagement.application.dto.workspaces.WorkspacesMapper;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.projectmanagement.domain.entities.Workspaces;
import org.projectmanagement.domain.repository.WorkspacesRepository;
import org.projectmanagement.domain.services.WorkspacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspacesServiceImpl implements WorkspacesService {
    private final WorkspacesRepository workspacesRepository;

    @Autowired
    public WorkspacesServiceImpl(WorkspacesRepository workspacesRepository) {
        this.workspacesRepository = workspacesRepository;
    }

    public WorkspacesRead createWorkspace(WorkspacesCreate workspace) {
        return null;
    }

    public WorkspacesRead findById(UUID id) {
        Workspaces workspace = workspacesRepository.findById(id).orElse(null);
        if (workspace == null) {
            return null;
        }
        return WorkspacesMapper.toWorkspacesRead(workspace);
    }

    public List<WorkspacesRead> findAllWorkspacesOfCompany(UUID companyId) {
        return workspacesRepository.findAllWorkspacesOfCompany(companyId)
                .stream().map(WorkspacesMapper::toWorkspacesRead).toList();
    }

    public WorkspacesRead updateWorkspace(UUID id, WorkspacesCreate workspace) {
        Workspaces existingWorkspace = workspacesRepository.findById(id).orElse(null);
        if (existingWorkspace == null) {
            return null;
        }

        Optional<Workspaces> updatedWorkspace = workspacesRepository.findByIdAndUpdate(id,
                new Workspaces(
                        id,
                        workspace.getName(),
                        workspace.getDescription(),
                        workspace.getCompanyId(),
                        existingWorkspace.getCreatedAt(),
                        existingWorkspace.getUpdatedAt()
                )
        );
        return updatedWorkspace.map(WorkspacesMapper::toWorkspacesRead).orElse(null);
    }

    public void deleteWorkspace(UUID id) {
        workspacesRepository.deleteById(id);
    }
}
