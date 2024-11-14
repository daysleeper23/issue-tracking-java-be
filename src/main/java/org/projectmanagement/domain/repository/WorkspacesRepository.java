package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Workspaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesRepository {
    Workspaces safeCopy(Workspaces workspace);
    Workspaces save(Workspaces workspace);

    Optional<Workspaces> findByIdAndUpdate(UUID id, Workspaces workspace);
    Optional<Workspaces> findById(UUID id);
    List<Workspaces> findAllWorkspaces(UUID companyId);

    void deleteById(UUID id);
}
