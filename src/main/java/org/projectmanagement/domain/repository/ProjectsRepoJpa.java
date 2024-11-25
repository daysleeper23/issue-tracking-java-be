package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectsRepoJpa extends JpaRepository<Projects, UUID> {

    @Query(value = "SELECT * " +
            "FROM projects p " +
            "WHERE p.workspace_id = :workspaceId ",
            nativeQuery = true)
    List<Projects> findByWorkspaceId(@Param("workspaceId") UUID workspaceId);

}
