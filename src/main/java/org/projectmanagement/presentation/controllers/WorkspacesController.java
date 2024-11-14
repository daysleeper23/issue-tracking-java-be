package org.projectmanagement.presentation.controllers;

import org.projectmanagement.application.dto.workspaces.WorkspacesCreate;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.projectmanagement.domain.services.WorkspacesService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}/workspaces")
@Validated
public class WorkspacesController {

    private final WorkspacesService workspacesService;

    @Autowired
    public WorkspacesController(WorkspacesService workspacesService) {
        this.workspacesService = workspacesService;
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<WorkspacesRead>>> getWorkspaces(@PathVariable UUID companyId) {
        return new ResponseEntity<>(
                new GlobalResponse<>(
                        HttpStatus.OK.value(),
                        workspacesService.findAllWorkspacesOfCompany(companyId)
                ),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<WorkspacesRead>> createWorkspace(@RequestBody WorkspacesCreate workspace) {
        WorkspacesRead createdWorkspace = workspacesService.createWorkspace(workspace);
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.CREATED.value(), createdWorkspace),
                HttpStatus.CREATED
        );
    }

}
