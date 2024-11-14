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

    public Optional<Workspaces> save(Workspaces workspace) {
        return Optional.empty();
    }

    public Optional<Workspaces> findById(UUID id) {
        return Optional.empty();
    }

    public List<Workspaces> findAllWorkspacesOfCompany(UUID companyId) {
        return null;
    }

    public Optional<Workspaces> findByIdAndUpdate(UUID id, Workspaces workspace) {
        return Optional.empty();
    }

    public void deleteById(UUID id) {

    }
}
