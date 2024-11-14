package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesRead;
import org.projectmanagement.application.services.WorkspacesMembersRolesServiceImpl;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}/{workspaceId}/members/roles")
public class WorkspacesMembersRolesController {
    private final WorkspacesMembersRolesServiceImpl wmrsi;

    public WorkspacesMembersRolesController(WorkspacesMembersRolesServiceImpl wmri) {
        this.wmrsi = wmri;
    }

    //get the roles for all members in a workspace
    @GetMapping
    public ResponseEntity<GlobalResponse<List<WorkspacesMembersRolesRead>>> getWorkspacesMembersRoles(
            @PathVariable UUID workspaceId
    ) {
        List<WorkspacesMembersRolesRead> wmrl = wmrsi
                .getWorkspacesMembersRoles(workspaceId);

        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.OK.value(), wmrl),
                HttpStatus.OK
        );
    }

    //probably don't need this as an end point: get the role for a user in a workspace using workspace id and user id
//    @GetMapping("/workspaces/{workspaceId}/members/{userId}/role")
//    public ResponseEntity<GlobalResponse<WorkspacesMembersRolesRead>> getWorkspacesMembersRoles(@PathVariable UUID userId, @PathVariable UUID workspaceId) {
//        Optional<WorkspacesMembersRolesRead> workspacesMembersRoles = workspacesMembersRolesService.getWorkspacesMembersRolesForUser(userId, workspaceId);
//
//        return workspacesMembersRoles
//                .map(workspacesMembersRolesRead -> new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), workspacesMembersRolesRead), HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(new GlobalResponse<>(HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND));
//    }

    //create a role for a user in a workspace == add a user to a workspace
    @PostMapping
    public ResponseEntity<GlobalResponse<WorkspacesMembersRolesRead>> createWorkspacesMembersRoles(
            @RequestBody @Valid WorkspacesMembersRolesCreate wmrCreate
    ) {
        WorkspacesMembersRolesRead wmrr = wmrsi
                .createWorkspacesMembersRoles(wmrCreate);

        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), wmrr), HttpStatus.CREATED);
    }

    //update role for a user in a workspace using its own id
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<WorkspacesMembersRolesRead>> updateWorkspacesMembersRoles(
            @PathVariable UUID id,
            @RequestBody WorkspacesMembersRolesCreate newWorkspacesMembersRoles
    ) {
        WorkspacesMembersRolesRead wmrr = wmrsi
                .updateWorkspacesMembersRoles(id, newWorkspacesMembersRoles);

        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), wmrr), HttpStatus.OK);
    }
}
