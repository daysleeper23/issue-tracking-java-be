package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreateDTO;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsMapper;
import org.projectmanagement.domain.entities.RolesPermissions;

public class PermissionsRolesServiceImpl {

    public RolesPermissions createRolesPermissions(RolesPermissionsCreateDTO dto) {
        RolesPermissions rolesPermissions = RolesPermissionsMapper.CreateDTOToRolesPermissions(dto);
        RolesPermissions createdRolesPermissions = null; // call to repo -> save to db
        return createdRolesPermissions;
    }
}
