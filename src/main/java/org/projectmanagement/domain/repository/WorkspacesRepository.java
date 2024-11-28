package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Workspaces;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesRepository {
    Optional<Workspaces> findByIdAndUpdate(UUID id, Workspaces workspace);

    List<Workspaces> findAllWorkspaces(UUID companyId);

    int deleteById(UUID id);

    Workspaces save(Workspaces build);

    Optional<Workspaces> findById(UUID id);
    Optional<Workspaces> findByIdForCompany(UUID id, UUID companyId);
}
