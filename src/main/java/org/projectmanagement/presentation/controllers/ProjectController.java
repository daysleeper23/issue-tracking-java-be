package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.Project.ProjectCreateDTO;
import org.projectmanagement.application.dto.Project.ProjectUpdateDTO;
import org.projectmanagement.domain.entities.Projects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.projectmanagement.application.services.ProjectServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectServiceImpl projectService;

    public ProjectController(ProjectServiceImpl projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<Projects>>> getProject() {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectService.getProjects()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Projects>> createProject(@RequestBody @Valid ProjectCreateDTO project) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), projectService.createProject(project)), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<Projects>> updateProject(@PathVariable UUID id, @RequestBody @Valid ProjectUpdateDTO project) {
        Projects updatedProject = projectService.updateProject(id, project);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), updatedProject), HttpStatus.OK);
    }

}
