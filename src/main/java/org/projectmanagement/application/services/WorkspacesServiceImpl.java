package org.projectmanagement.application.services;

import jakarta.transaction.Transactional;
import org.projectmanagement.application.dto.workspaces.WorkspacesCreate;
import org.projectmanagement.application.dto.workspaces.WorkspacesMapper;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.projectmanagement.application.dto.workspaces.WorkspacesUpdate;
import org.projectmanagement.domain.entities.Workspaces;
import org.projectmanagement.domain.repository.WorkspacesRepository;
import org.projectmanagement.domain.services.WorkspacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    public Optional<WorkspacesRead> createWorkspace(UUID companyId, WorkspacesCreate workspace) {
        Workspaces newWorkspace = workspacesRepository.save(
                Workspaces.builder()
                        .name(workspace.getName())
                        .description(workspace.getDescription())
                        .companyId(companyId)
                        .isDeleted(false)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );
        return Optional.of(WorkspacesMapper.toWorkspacesRead(newWorkspace));
    }

    public Optional<WorkspacesRead> findById(UUID id) {
        Optional<Workspaces> workspace = workspacesRepository.findById(id);
        return workspace.map(WorkspacesMapper::toWorkspacesRead);
    }

    public List<WorkspacesRead> findAllWorkspaces(UUID companyId) {
        return workspacesRepository.findAllWorkspaces(companyId)
                .stream().map(WorkspacesMapper::toWorkspacesRead).toList();
    }

    @Transactional
    public Optional<WorkspacesRead> updateWorkspace(UUID id, WorkspacesUpdate wu) {
        Optional<Workspaces> existingWorkspace = workspacesRepository.findById(id);
        if (existingWorkspace.isEmpty()) {
            return Optional.empty();
        }

        System.out.println("Existing workspace: " + existingWorkspace.get());
        // Use MapStruct to update only non-null fields
        WorkspacesMapper.INSTANCE.toWorkspacesFromWorkspacesUpdate(wu, existingWorkspace.get());

        System.out.println("Updated workspace: " + existingWorkspace.get());
        Optional<Workspaces> updatedWorkspace = workspacesRepository.findByIdAndUpdate(
                id,
                existingWorkspace.get()
        );

        return updatedWorkspace.map(WorkspacesMapper::toWorkspacesRead);
    }

    @Transactional
    public void deleteWorkspace(UUID id) {
        workspacesRepository.deleteById(id);
    }
}
