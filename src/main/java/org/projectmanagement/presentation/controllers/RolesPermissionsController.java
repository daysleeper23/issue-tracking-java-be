package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreate;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsRead;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsUpdate;
import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.domain.services.RolesPermissionsService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}/rolesPermissions")
public class RolesPermissionsController {

    private final RolesPermissionsService rolesPermissionsService;

    RolesPermissionsController(RolesPermissionsService rolesPermissionsService) {
        this.rolesPermissionsService = rolesPermissionsService;
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<RolesPermissionsRead>>> getAllRolesPermissions(@PathVariable UUID companyId) {
        List<RolesPermissionsRead> rolesPermissions = rolesPermissionsService.getAllRolesPermissionsForCompany(companyId);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), rolesPermissions), HttpStatus.OK);
    }

    @PatchMapping("/{roleId}")
    public ResponseEntity<GlobalResponse<List<RolesPermissions>>> addPermissionsToRoles(@PathVariable @Valid UUID roleId,@RequestBody @Valid  RolesPermissionsUpdate dto) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), rolesPermissionsService.addPermissionsToRole(roleId, dto)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<List<RolesPermissions>>> getRolesPermissions(@PathVariable @Valid UUID companyId, @RequestBody @Valid RolesPermissionsCreate dto) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), rolesPermissionsService.createRolePermissions(companyId, dto)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<GlobalResponse<String>> removePermissionsFromRole(@PathVariable @Valid UUID roleId, @RequestBody @Valid RolesPermissionsUpdate dto) {
        rolesPermissionsService.removePermissionsFromRole(roleId, dto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), "Permissions Removed from role"), HttpStatus.OK);
    }
}
