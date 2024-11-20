package org.projectmanagement.application.services;


import org.projectmanagement.application.dto.projects.ProjectMapper;
import org.projectmanagement.application.dto.projects.ProjectsCreate;
import org.projectmanagement.application.dto.projects.ProjectsUpdate;
import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.domain.repository.ProjectsRepository;
import org.projectmanagement.domain.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectsRepository projectsRepository;

    @Autowired
    ProjectServiceImpl(ProjectsRepository projectsRepository){
        this.projectsRepository = projectsRepository;
    }

    public Optional<Projects> getProjectById(UUID id) {
        Projects project = projectsRepository.findOneById(id).orElse(null);
        if (project == null){
            throw new ResourceNotFoundException("Project with id: " + id + " was not found.");
        }
        return Optional.of(project);
    }

    public List<Projects> getProjectsByWorkspaceId(UUID workspaceId) {
        List<Projects> projects = projectsRepository.findAllFromWorkspace(workspaceId);
        return projects;
    }

    public Projects createProject(ProjectsCreate dto){
        Projects project = projectsRepository.save(
            Projects.builder()
                    //.id(UUID.randomUUID())
                    .name(dto.name())
                    .description(dto.description())
                    .endDate(dto.endDate())
                    .startDate(dto.startDate())
                    .priority(dto.priority())
                    .status(dto.status())
                    .leaderId(dto.leaderId())
                    .workspaceId(dto.workspaceId())
                    //.createdAt(Instant.now())
                    //.updatedAt(Instant.now())
                    .build()
        );
        return projectsRepository.save(project);
    }

    public Projects updateProject(UUID id, ProjectsUpdate dto) {
        Projects projectToUpdate = projectsRepository.findOneById(id).orElse(null);

        if (projectToUpdate == null) {
            return null;
        }
        ProjectMapper.INSTANCE.toProjectsFromProjectsUpdateDTO(dto, projectToUpdate);

        return projectsRepository.save(projectToUpdate);
    }

    public void deleteProject(UUID id) {
        Projects projectToDelete = projectsRepository.findOneById(id).orElse(null);

        if (projectToDelete == null) {
            throw new ResourceNotFoundException("Project with id: " + id + " was not found.");
        }

        projectToDelete.setIsDeleted(true);
        projectsRepository.save(projectToDelete);
    }

}
