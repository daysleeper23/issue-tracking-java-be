package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Workspaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacesRepository extends JpaRepository<Workspaces, UUID> {

    @Modifying
    @Query("UPDATE Workspaces w SET w.name = :#{#workspace.name}, " +
            "w.description = :#{#workspace.description}, " +
            "w.updatedAt = CURRENT_TIMESTAMP WHERE w.id = :id")
    Optional<Workspaces> findByIdAndUpdate(UUID id, Workspaces workspace);

    @Query("SELECT w FROM Workspaces w WHERE w.companyId = :companyId")
    List<Workspaces> findAllWorkspaces(UUID companyId);

    @Query("DELETE FROM Workspaces w WHERE w.id = :id")
    void deleteById(UUID id);
}
