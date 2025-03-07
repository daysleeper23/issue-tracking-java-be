package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.projects.ProjectsCreate;
import org.projectmanagement.application.dto.projects.ProjectsUpdate;
import org.projectmanagement.domain.entities.Projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    Optional<Projects> getProjectById(UUID id);

    List<Projects> getProjectsByWorkspaceId(UUID workspaceId);

    Projects createProject(ProjectsCreate dto);

    Projects updateProject(UUID id, ProjectsUpdate dto);

    void deleteProject(UUID id);
}
