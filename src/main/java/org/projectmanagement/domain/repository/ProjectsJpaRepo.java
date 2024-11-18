package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectsJpaRepo extends JpaRepository<Projects, UUID> {
    List<Projects> findByWorkspaceId(UUID workspaceId);
}
