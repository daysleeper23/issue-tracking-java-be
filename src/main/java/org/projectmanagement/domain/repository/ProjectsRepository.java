package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectsRepository {
    Projects save(Projects project);

    Projects deleteOneById(UUID id);

    Optional<Projects> findOneById(UUID id);

    List<Projects> findAllFromWorkspace(UUID workspaceId);

}
