package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.repository.ProjectsRepoJpa;
import org.projectmanagement.domain.repository.ProjectsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectsRepoImpl implements ProjectsRepository {

    private final ProjectsRepoJpa projectsRepoJpa;

    public ProjectsRepoImpl(ProjectsRepoJpa projectsRepoJpa) {
        this.projectsRepoJpa = projectsRepoJpa;
    }

    @Override
    public Projects save(Projects project) {
        return projectsRepoJpa.save(project);
    }

    @Override
    public Projects deleteOneById(UUID id) {
       Projects project = projectsRepoJpa.findById(id).orElse(null);
       project.setIsDeleted(true);
       return projectsRepoJpa.save(project);
    }

    @Override
    public Optional<Projects> findOneById(UUID id) {
        return projectsRepoJpa.findById(id);
    }

    @Override
    public List<Projects> findAllFromWorkspace(UUID workspaceId) {
        return projectsRepoJpa.findByWorkspaceId(workspaceId);
    }

    @Override
    public int deleteProjectsByWorkspaceId(UUID workspaceId) {
        return projectsRepoJpa.deleteProjectsByWorkspaceId(workspaceId);
    }
}
