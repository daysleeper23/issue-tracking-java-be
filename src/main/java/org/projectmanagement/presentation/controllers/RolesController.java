package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.roles.RolesCreate;
import org.projectmanagement.application.services.RolesServiceImpl;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}")
public class RolesController {
    private final RolesServiceImpl rolesService;

    public RolesController(RolesServiceImpl rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping("/roles")
    public ResponseEntity<GlobalResponse<List<Roles>>> getRoles(@PathVariable UUID companyId) {
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.OK.value(), rolesService.findAllRoles(companyId)),
                HttpStatus.OK
        );
    }

    @PostMapping("/roles")
    public ResponseEntity<GlobalResponse<Roles>> createRole(@Valid RolesCreate newRole) {
        Roles createdRole = rolesService.createRole(newRole);
        if (createdRole == null) {
            return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CONFLICT.value(), null), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), createdRole), HttpStatus.CREATED);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<GlobalResponse<Roles>> updateRole(@Valid Roles role) {
        Roles updatedRole = rolesService.updateRoleName(role);
        if (updatedRole == null) {
            return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), updatedRole), HttpStatus.OK);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteRole(@PathVariable UUID id) {
        rolesService.deleteRole(id);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.NO_CONTENT.value(), null), HttpStatus.NO_CONTENT);
    }
}
