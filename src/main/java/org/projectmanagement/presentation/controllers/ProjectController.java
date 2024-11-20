package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.projects.ProjectsCreate;
import org.projectmanagement.application.dto.projects.ProjectsUpdate;
import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.services.ProjectService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.projectmanagement.application.services.ProjectServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}/{workspaceId}/projects")
public class ProjectController {
    @Autowired
    private final ProjectService projectService;

    public ProjectController(ProjectServiceImpl projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<Optional<Projects>>> getProject(@PathVariable @Valid UUID id) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectService.getProjectById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<Projects>>> getProjectsByWorkspaceId(@PathVariable @Valid UUID workspaceId) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectService.getProjectsByWorkspaceId(workspaceId)), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<GlobalResponse<Projects>> createProject(@RequestBody @Valid ProjectsCreate project) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), projectService.createProject(project)), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<Projects>> updateProject(@PathVariable @Valid UUID id, @RequestBody @Valid ProjectsUpdate project) {
        Projects updatedProject = projectService.updateProject(id, project);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), updatedProject), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteProject(@PathVariable @Valid UUID id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), "Project Deleted"), HttpStatus.OK);
    }

}
