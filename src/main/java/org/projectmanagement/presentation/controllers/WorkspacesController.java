package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.workspaces.WorkspacesCreate;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.projectmanagement.application.dto.workspaces.WorkspacesUpdate;
import org.projectmanagement.domain.services.WorkspacesService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
                        workspacesService.findAllWorkspaces(companyId)
                ),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<WorkspacesRead>> createWorkspace(@RequestBody WorkspacesCreate workspace) {
        Optional<WorkspacesRead> createdWorkspace = workspacesService.createWorkspace(workspace);
        return createdWorkspace
                .map(workspacesRead -> new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.CREATED.value(), workspacesRead),
                    HttpStatus.CREATED
                ))
                .orElseGet(() -> new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.CONFLICT.value(), null),
                    HttpStatus.CONFLICT
                ));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<WorkspacesRead>> updateWorkspace(@PathVariable UUID id, @RequestBody @Valid WorkspacesUpdate workspace) {
        Optional<WorkspacesRead> updatedWorkspace = workspacesService.updateWorkspace(id, workspace);
        return updatedWorkspace
                .map(workspacesRead -> new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.OK.value(), workspacesRead),
                    HttpStatus.OK
                ))
                .orElseGet(() -> new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.NOT_FOUND.value(), null),
                    HttpStatus.NOT_FOUND
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteWorkspace(@PathVariable UUID id) {
        workspacesService.deleteWorkspace(id);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.NO_CONTENT.value(), null), HttpStatus.NO_CONTENT);
    }

}
