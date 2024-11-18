package org.projectmanagement.presentation.controllers;

import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rolesPermissions")
public class RolesPermissionsController {

    // should I return all permissions for all roles or only permissions by role?
    @GetMapping
    public ResponseEntity<GlobalResponse<List<RolesPermissions>>> getRolesPermissions(@PathVariable UUID id) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), null), HttpStatus.OK);
    }
}
