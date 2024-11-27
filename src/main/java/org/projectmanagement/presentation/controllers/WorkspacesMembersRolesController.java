package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesRead;
import org.projectmanagement.domain.services.WorkspacesMembersRolesService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/{companyId}/{workspaceId}/members/roles")
public class WorkspacesMembersRolesController {

    private final WorkspacesMembersRolesService wmrs;

    @Autowired
    public WorkspacesMembersRolesController(WorkspacesMembersRolesService wmrs) {
        this.wmrs = wmrs;
    }

    //get the roles for all members in a workspace
    @GetMapping
    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication, #workspaceId, {'WORKSPACE_READ_ALL', 'WORKSPACE_READ_ONE'})")
    public ResponseEntity<GlobalResponse<List<WorkspacesMembersRolesRead>>> getWorkspacesMembersRoles(
            @PathVariable UUID workspaceId
    ) {
        List<WorkspacesMembersRolesRead> wmrl = wmrs
                .getMembersRolesForWorkspace(workspaceId);

        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.OK.value(), wmrl),
                HttpStatus.OK
        );
    }

    //create a role for a user in a workspace == add a user to a workspace
    @PostMapping
    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication, #workspaceId, {'WORKSPACE_UPDATE_ALL', 'WORKSPACE_UPDATE_ONE'})")
    public ResponseEntity<GlobalResponse<WorkspacesMembersRolesRead>> createWorkspacesMembersRoles(
            @PathVariable UUID workspaceId,
            @RequestBody @Valid WorkspacesMembersRolesCreate wmrCreate
    ) {
        WorkspacesMembersRolesRead wmrr = wmrs
                .createMembersRolesForWorkspace(workspaceId, wmrCreate);

        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), wmrr), HttpStatus.CREATED);
    }

    //update role for a user in a workspace using its own id
    @PutMapping("/{id}")
    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication, #workspaceId, {'WORKSPACE_UPDATE_ALL', 'WORKSPACE_UPDATE_ONE'})")
    public ResponseEntity<GlobalResponse<WorkspacesMembersRolesRead>> updateWorkspacesMembersRoles(
            @PathVariable UUID id,
            @PathVariable UUID workspaceId,
            @RequestBody WorkspacesMembersRolesCreate newWorkspacesMembersRoles
    ) {
        WorkspacesMembersRolesRead wmrr = wmrs
                .updateWorkspacesMembersRoles(id, workspaceId, newWorkspacesMembersRoles);

        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), wmrr), HttpStatus.OK);
    }

    //delete role for a user in a workspace using its own id
    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication, #workspaceId, {'WORKSPACE_DELETE_ALL', 'WORKSPACE_DELETE_ONE'})")
    public ResponseEntity<GlobalResponse<Void>> deleteWorkspacesMembersRoles(
            @PathVariable UUID id,
            @PathVariable UUID workspaceId
    ) {
        wmrs.deleteWorkspacesMembersRoles(id, workspaceId);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.NO_CONTENT.value(), null), HttpStatus.NO_CONTENT);
    }
}
