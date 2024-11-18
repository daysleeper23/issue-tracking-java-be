package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.repository.ProjectsJpaRepo;
import org.projectmanagement.domain.repository.ProjectsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectsRepoImpl implements ProjectsRepository {

    private final ProjectsJpaRepo projectsJpaRepo;

    public ProjectsRepoImpl(ProjectsJpaRepo projectsJpaRepo) {
        this.projectsJpaRepo = projectsJpaRepo;
    }

    @Override
    public Projects save(Projects project) {
        return projectsJpaRepo.save(project);
    }

    @Override
    public Projects deleteOneById(UUID id) {
        return null;
    }

    @Override
    public Optional<Projects> findOneById(UUID id) {
        return projectsJpaRepo.findById(id);
    }

    @Override
    public List<Projects> findAllFromWorkspace(UUID workspaceId) {
        return projectsJpaRepo.findByWorkspaceId(workspaceId);
    }
}
