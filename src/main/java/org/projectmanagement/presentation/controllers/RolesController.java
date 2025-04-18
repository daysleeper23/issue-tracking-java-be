package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectmanagement.application.dto.roles.RolesCreate;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.services.RolesService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}/roles")
@Validated
public class RolesController {
    private final RolesService rolesService;

    @Autowired
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<Roles>>> getRoles(@PathVariable UUID companyId) {
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.OK.value(), rolesService.findAllRoles(companyId)),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Roles>> createRole(
            @PathVariable UUID companyId
            , @RequestBody @Valid RolesCreate newRole
    ) {
        Roles createdRole = rolesService.createRole(companyId, newRole);
        if (createdRole == null) {
            return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CONFLICT.value(), null), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), createdRole), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<Roles>> updateRole(
            @PathVariable UUID id,
            @PathVariable UUID companyId,
            @RequestBody @Valid RolesCreate role
    ) {
        Roles updatedRole = rolesService.updateRoleName(id, companyId, role);
        if (updatedRole == null) {
            return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), updatedRole), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteRole(@PathVariable @NotNull UUID id) {
        Boolean ok = rolesService.deleteRole(id);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.NO_CONTENT.value(), null), HttpStatus.NO_CONTENT);
    }
}
