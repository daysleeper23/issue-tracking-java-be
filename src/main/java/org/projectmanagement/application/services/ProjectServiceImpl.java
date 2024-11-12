package org.projectmanagement.application.services;


import org.projectmanagement.application.dto.Project.ProjectCreateDTO;
import org.projectmanagement.application.dto.Project.ProjectUpdateDTO;
import org.projectmanagement.domain.entities.Projects;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl {

    public List<Projects> getProjects() {
        List<Projects> pList = null; //calling repo then getting projects from db
        return pList;
    }

    public Projects createProject(ProjectCreateDTO project){
        Projects createdProject = null; // calling repo then saving project to db
        return createdProject;
    }

    public Projects updateProject(UUID id, ProjectUpdateDTO project) {
        Projects updatedProject = null; // calling repo then updating project to db
        return updatedProject;
    }
}
