package org.projectmanagement.application.services;


import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreate;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsUpdate;
import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.domain.repository.RolesPermissionsRepository;
import org.projectmanagement.domain.repository.RolesRepository;
import org.projectmanagement.domain.services.RolesPermissionsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RolesPermissionsServiceImpl implements RolesPermissionsService {
    private final RolesPermissionsRepository rolesPermissionsRepository;
    private final RolesRepository rolesRepository;

    RolesPermissionsServiceImpl(RolesPermissionsRepository rolesPermissionsRepository
            , RolesRepository rolesRepository
    ) {
        this.rolesPermissionsRepository = rolesPermissionsRepository;
        this.rolesRepository = rolesRepository;
    }


    @Override
    public RolesPermissions getOneById(UUID id) {
        RolesPermissions rolesPermissionsFromDB = rolesPermissionsRepository.findById(id).orElse(null);
        if (rolesPermissionsFromDB == null) {
            throw new ResourceNotFoundException("Role Permission with id: " + id + " was not found.");
        }
        return rolesPermissionsFromDB;
    }

    @Override
    public List<UUID> getAllPermissionsOfRoleByRoleId(UUID roleId) {
        return rolesPermissionsRepository.findAllPermissionsOfRoleByRoleId(roleId);
    }

    @Override
    public List<RolesPermissions> createRolePermissions(RolesPermissionsCreate dto) {
        List<RolesPermissions> rolesPermissions = new ArrayList<>();

        Roles roleFromDB = rolesRepository.findById(dto.roleId()).orElse(null);

        if (roleFromDB == null) {
            throw new ResourceNotFoundException("Role with id:" + dto.roleId() + " was not found.");
       }

        for (UUID permissionId : dto.permissions()) {
            rolesPermissions.add(
                    rolesPermissionsRepository.save(new RolesPermissions(UUID.randomUUID(), dto.roleId(), permissionId))
            );
        }
        return rolesPermissions;
    }

    @Override
    public List<RolesPermissions> addPermissionsToRole(UUID roleId, RolesPermissionsUpdate dto) {
        List<RolesPermissions> rolesPermissions = new ArrayList<>();

        Roles roleFromDB = rolesRepository.findById(roleId).orElse(null);

        if (roleFromDB == null) {
            throw new ResourceNotFoundException("Role with id:" + roleId + " was not found.");
        }

        List<UUID> permissionsOfRole = rolesPermissionsRepository.findAllPermissionsOfRoleByRoleId(roleId);

        for (UUID permissionId : dto.permissions()) {
            if (!permissionsOfRole.contains(permissionId)) {
                rolesPermissions.add(
                        rolesPermissionsRepository.save(new RolesPermissions(UUID.randomUUID(), roleId, permissionId))
                );
            }
        }

        return rolesPermissions;
    }

    @Override
    public void removePermissionsFromRole(UUID roleId, RolesPermissionsUpdate dto) {
        Roles roleFromDB = rolesRepository.findById(roleId).orElse(null);

        if (roleFromDB == null) {
            throw new ResourceNotFoundException("Role with id:" + roleId + " was not found.");
        }

        List<RolesPermissions> rolesPermissions = rolesPermissionsRepository.findAllRolePermissionsByRoleId(roleId);


        for (UUID permissionId : dto.permissions()) {
            //call repo to delete
            rolesPermissions.stream()
                    .filter(rolePermission -> rolePermission.getPermissionId().equals(permissionId))
                    .findFirst() // Find the first matching RolesPermissions entity
                    .ifPresent(rolePermission -> rolesPermissionsRepository.deleteById(rolePermission.getId())); // Delete by entity ID
        }
    }
}
