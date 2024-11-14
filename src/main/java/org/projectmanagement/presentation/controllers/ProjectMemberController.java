package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.project_members.ProjectMemberCreateDTO;
import org.projectmanagement.application.services.ProjectMembersServiceImpl;
import org.projectmanagement.domain.entities.ProjectMembers;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/projectMembers")
public class ProjectMemberController {
    private ProjectMembersServiceImpl projectMembersService;

    public ProjectMemberController(ProjectMembersServiceImpl projectMembersService) {
        this.projectMembersService = projectMembersService;
    }

    //naming for mapping is hard -_-
    //return all members of given project
    @GetMapping("byProject/{projectId}")
    public ResponseEntity<GlobalResponse<List<ProjectMembers>>> getAllMembersByProjectId(@Valid @PathVariable UUID projectId) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectMembersService.getAllProjectMembersByProjectId(projectId)), HttpStatus.OK);
    }

    //return all projects of a given member
    @GetMapping("byUser/{userId}")
    public ResponseEntity<GlobalResponse<List<ProjectMembers>>> getAllProjectsMemberIsPartOfByUserId(@Valid @PathVariable UUID userId) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectMembersService.getAllProjectsMemberIsPartOfByUserId(userId)), HttpStatus.OK);
    }

/*
    Not implemented until we decide upon mapstruct
    @PostMapping
    public ResponseEntity<GlobalResponse<ProjectMembers>> createProjectMember(@Valid @PathVariable  userId, @Valid ProjectMemberCreateDTO dto) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectMembersService.createProjectMember(dto)), HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<GlobalResponse<ProjectMembers>> updateProjectMember(@Valid @PathVariable  userId, @Valid ProjectMemberUpdateDTO dto) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), projectMembersService.updateProjectMember(dto)), HttpStatus.OK);
    }

*/
    @DeleteMapping("{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteProjectMemberById(@Valid @PathVariable UUID id) {
        projectMembersService.deleteProjectMemberById(id);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), null), HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}/{userId}")
    public ResponseEntity<GlobalResponse<Void>> deleteProjectMemberByProjectIdAndUserId(@Valid @PathVariable UUID projectId,@Valid @PathVariable UUID userId) {
        projectMembersService. deleteProjectMemberByProjectIdAndUserId(projectId, userId);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), null), HttpStatus.OK);
    }




}
