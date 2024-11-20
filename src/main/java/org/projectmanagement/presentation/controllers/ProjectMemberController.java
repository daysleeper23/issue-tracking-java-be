package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.project_members.ProjectMemberCreate;
import org.projectmanagement.application.dto.project_members.ProjectMemberUpdate;
import org.projectmanagement.application.services.ProjectMembersServiceImpl;
import org.projectmanagement.domain.entities.ProjectMembers;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{projectId}/projectMembers")
public class ProjectMemberController {
    private ProjectMembersServiceImpl projectMembersService;

    public ProjectMemberController(ProjectMembersServiceImpl projectMembersService) {
        this.projectMembersService = projectMembersService;
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<ProjectMembers>>> getAllMembersByProjectId(@Valid @PathVariable UUID projectId) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectMembersService.getAllProjectMembersByProjectId(projectId)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<ProjectMembers>> getAllProjectsMemberIsPartOfByUserId(@Valid @PathVariable UUID id) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectMembersService.getProjectMemberById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<ProjectMembers>> createProjectMember(@RequestBody @Valid ProjectMemberCreate dto) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), projectMembersService.createProjectMember(dto)), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<ProjectMembers>> updateProjectMember(@Valid @PathVariable UUID id, @Valid ProjectMemberUpdate dto) {
        ProjectMembers updatedProjectMember = projectMembersService.updateProjectMember(id, dto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), updatedProjectMember), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteProjectMemberById(@Valid @PathVariable UUID id) {
        projectMembersService.deleteProjectMemberById(id);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), null), HttpStatus.OK);
    }

}
