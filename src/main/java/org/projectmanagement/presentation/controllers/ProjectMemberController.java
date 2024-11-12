package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.Project.ProjectCreateDTO;
import org.projectmanagement.application.dto.ProjectMember.ProjectMemberCreateDTO;
import org.projectmanagement.application.services.ProjectMembersServiceImpl;
import org.projectmanagement.domain.entities.ProjectMembers;
import org.projectmanagement.domain.entities.Projects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projectMembers")
public class ProjectMemberController {
    private ProjectMembersServiceImpl projectMembersService;

    public ProjectMemberController(ProjectMembersServiceImpl projectMembersService) {
        this.projectMembersService = projectMembersService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<List<ProjectMembers>>> getMembersByProjectId(@PathVariable UUID id) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectMembersService.getMembersByProjectId(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<ProjectMembers>> createProjectMember(@RequestBody @Valid ProjectMemberCreateDTO projectMember) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), projectMembersService.createProjectMember(projectMember)), HttpStatus.CREATED);
    }
}
