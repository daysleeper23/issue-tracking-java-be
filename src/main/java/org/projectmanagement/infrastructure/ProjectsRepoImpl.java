package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.repository.ProjectsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectsRepoImpl implements ProjectsRepository {
    private final InMemoryDatabase inMemoryDatabase;

    public ProjectsRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }
    @Override
    public Projects save(Projects project) {
        return inMemoryDatabase.saveProject(project);
    }

    @Override
    public Projects archiveOneById(UUID id) {
        return null;
    }

    @Override
    public Optional<Projects> findOneById(UUID id) {
        return inMemoryDatabase.projects.stream().filter(project -> project.getId().equals(id)).findFirst();
    }

    @Override
    public List<Projects> findAllFromWorkspace(UUID workspaceId) {
        return inMemoryDatabase.projects.stream().filter(project -> project.getWorkspaceId().equals(workspaceId)).toList();
    }
}
