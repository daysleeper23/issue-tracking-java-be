package org.projectmanagement.domain.services;

//import org.projectmanagement.application.dto.projects.ProjectsCreateDTO;
//import org.projectmanagement.application.dto.projects.ProjectsUpdateDTO;
import org.projectmanagement.domain.entities.Projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    Optional<Projects> getProjectById(UUID id);

    List<Projects> getProjectsByWorkspaceId(UUID workspaceId);

//    Projects createProject(ProjectsCreateDTO dto);
//
//    Projects updateProject(UUID id, ProjectsUpdateDTO dto);
}
