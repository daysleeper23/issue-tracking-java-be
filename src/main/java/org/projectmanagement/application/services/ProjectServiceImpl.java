package org.projectmanagement.application.services;


import org.projectmanagement.application.dto.projects.ProjectMapper;
import org.projectmanagement.application.dto.projects.ProjectsCreate;
import org.projectmanagement.application.dto.projects.ProjectsUpdate;
import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.entities.Workspaces;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.domain.repository.ProjectsRepository;
import org.projectmanagement.domain.repository.TasksRepository;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.repository.WorkspacesRepository;
import org.projectmanagement.domain.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectsRepository projectsRepository;
    private final TasksRepository tasksRepository;
    private final WorkspacesRepository workspacesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    ProjectServiceImpl(ProjectsRepository projectsRepository,
                       TasksRepository tasksRepository,
                       WorkspacesRepository workspacesRepository,
                       UsersRepository usersRepository){
        this.projectsRepository = projectsRepository;
        this.tasksRepository = tasksRepository;
        this.workspacesRepository = workspacesRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<Projects> getProjectById(UUID id) {
        Projects project = projectsRepository.findOneById(id).orElse(null);
        if (project == null){
            throw new ResourceNotFoundException("Project with id: " + id + " was not found.");
        }
        return Optional.of(project);
    }

    public List<Projects> getProjectsByWorkspaceId(UUID workspaceId) {
        Workspaces workspace = workspacesRepository.findById(workspaceId).orElse(null);

        if (workspace == null){
            throw new ResourceNotFoundException("Workspace with id: " + workspaceId + " was not found.");
        }

        return projectsRepository.findAllFromWorkspace(workspaceId);
    }

    public Projects createProject(ProjectsCreate dto){
        Workspaces workspace = workspacesRepository.findById(dto.workspaceId()).orElse(null);
        if (workspace == null){
            throw new ResourceNotFoundException("Workspace with id: " + dto.workspaceId() + " was not found.");
        }

        if (dto.leaderId() != null) {
            Users userFromDb = usersRepository.findById(dto.leaderId()).orElse(null);

            if (userFromDb == null){
                throw new ResourceNotFoundException("User with id: " + dto.leaderId() + " was not found.");
            }
        }

        return projectsRepository.save(
                Projects.builder()
                        .name(dto.name())
                        .description(dto.description())
                        .endDate(dto.endDate())
                        .startDate(dto.startDate())
                        .priority(dto.priority() == null ? 0 : dto.priority())
                        .status(dto.status())
                        .leaderId(dto.leaderId())
                        .workspaceId(dto.workspaceId())
                        .isDeleted(false)
                        .build()
        );
    }

    @Transactional
    public Projects updateProject(UUID id, ProjectsUpdate dto) {
        Projects projectToUpdate = projectsRepository.findOneById(id).orElse(null);

        if (projectToUpdate == null) {
            return null;
        }
        ProjectMapper.INSTANCE.toProjectsFromProjectsUpdateDTO(dto, projectToUpdate);

        return projectsRepository.save(projectToUpdate);
    }

    @Transactional
    public void deleteProject(UUID id) {
        Projects projectToDelete = projectsRepository.findOneById(id).orElse(null);

        if (projectToDelete == null) {
            throw new ResourceNotFoundException("Project with id: " + id + " was not found.");
        }

        List<Tasks> projectRelatedTasks = tasksRepository.findByProjectId(id);

        projectRelatedTasks.forEach(task -> task.setStatus(DefaultStatus.ARCHIVED));

        tasksRepository.saveAll(projectRelatedTasks);

        projectToDelete.setIsDeleted(true);
        projectsRepository.save(projectToDelete);
    }
}
