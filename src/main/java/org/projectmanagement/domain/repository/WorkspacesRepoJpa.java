package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Workspaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspacesRepoJpa extends JpaRepository<Workspaces, UUID> {

    @Query(value = "SELECT * " +
                    "FROM Workspaces " +
                    "WHERE company_id = :companyId " +
                    "AND is_deleted = false"
            , nativeQuery = true
    )
    List<Workspaces> findAllByCompanyId(UUID companyId);

    @Modifying
    @Query(value = "UPDATE Workspaces w " +
                    "SET is_deleted = true" +
                    "WHERE id = :id"
            , nativeQuery = true
    )
    void deleteById(UUID id);

    @Modifying
    @Query(value = "UPDATE Workspaces w " +
                    "SET w.name = :#{#workspace.name}, " +
                        "w.description = :#{#workspace.description}, " +
                        "w.updatedAt = CURRENT_TIMESTAMP " +
                    "WHERE w.id = :id" +
                    "AND w.is_deleted = false"
            , nativeQuery = true)
    Optional<Workspaces> updateById(@Param("id") UUID id, @Param("workspace") Workspaces workspace);
}
