package org.projectmanagement.infrastructure;

import org.projectmanagement.application.dto.workspaces.WorkspacesCreate;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.projectmanagement.domain.entities.Workspaces;
import org.projectmanagement.domain.repository.WorkspacesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WorkspacesRepoImpl implements WorkspacesRepository {
    private final InMemoryDatabase inMemoryDatabase;

    public WorkspacesRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    //return a complete copy of the object instead of its reference
    public Workspaces safeCopy(Workspaces workspace) {
        return new Workspaces(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getCompanyId(),
                workspace.getIsDeleted(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }

    public Workspaces save(Workspaces workspace) {
        Workspaces newWorkspace = inMemoryDatabase.saveWorkspace(workspace);
        return safeCopy(newWorkspace);
    }

    public Optional<Workspaces> findById(UUID id) {
        return Optional.ofNullable(
                safeCopy(inMemoryDatabase.getActiveWorkspaceById(id))
        );
    }

    public List<Workspaces> findAllWorkspaces(UUID companyId) {
        return inMemoryDatabase.getActiveWorkspacesByCompany(companyId).stream().map(this::safeCopy).toList();
    }

    public Optional<Workspaces> findByIdAndUpdate(UUID id, Workspaces workspace) {
        Workspaces updatedWorkspace = inMemoryDatabase.getWorkspaceByIdAndUpdate(id, workspace);
        return safeCopy(updatedWorkspace) == null ? Optional.empty() : Optional.of(safeCopy(updatedWorkspace));
    }

    public void deleteById(UUID id) {
        inMemoryDatabase.deleteWorkspaceById(id);
    }
}
