package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Workspaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesRepository {
    Optional<Workspaces> save(Workspaces workspace);
    Optional<Workspaces> findByIdAndUpdate(UUID id, Workspaces workspace);
    void deleteById(UUID id);

    Optional<Workspaces> findById(UUID id);
    List<Workspaces> findAllWorkspacesOfCompany(UUID companyId);
}
