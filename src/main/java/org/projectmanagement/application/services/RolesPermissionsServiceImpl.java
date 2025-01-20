package org.projectmanagement.application.services;


import jakarta.transaction.Transactional;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreate;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsRead;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsUpdate;
import org.projectmanagement.domain.entities.Permissions;
import org.projectmanagement.domain.entities.RolesPermissions;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.exceptions.ResourceAlreadyExistsException;
import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.domain.exceptions.SystemRoleUpdateException;
import org.projectmanagement.domain.repository.PermissionsRepository;
import org.projectmanagement.domain.repository.RolesPermissionsRepository;
import org.projectmanagement.domain.repository.RolesRepository;
import org.projectmanagement.domain.services.RolesPermissionsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class RolesPermissionsServiceImpl implements RolesPermissionsService {
    private final RolesPermissionsRepository rolesPermissionsRepository;
    private final RolesRepository rolesRepository;
    private final PermissionsRepository permissionsRepository;

    RolesPermissionsServiceImpl(RolesPermissionsRepository rolesPermissionsRepository
            , RolesRepository rolesRepository
            , PermissionsRepository permissionsRepository
    ) {
        this.rolesPermissionsRepository = rolesPermissionsRepository;
        this.rolesRepository = rolesRepository;
        this.permissionsRepository = permissionsRepository;
    }


    @Override
    public List<RolesPermissionsRead> getAllRolesPermissionsForCompany(UUID companyId) {
        return rolesPermissionsRepository.findAllRolesPermissionsForCompany(companyId);
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
    public List<RolesPermissions> createRolePermissions(UUID companyId, RolesPermissionsCreate dto) {
        List<RolesPermissions> rolesPermissions = new ArrayList<>();


        Roles roleFromDB = rolesRepository.findByExactName(dto.roleName(), companyId).orElse(null);

        if (roleFromDB != null) {
            throw new ResourceAlreadyExistsException("Role with name: " + dto.roleName() + " already exists.");
       }
        Roles createdRole = rolesRepository.save(Roles.builder()
                        .name(dto.roleName())
                        .companyId(companyId)
                        .isSystemRole(false)
                        .isDeleted(false)
                        .build());

        List<UUID> permissions = permissionsRepository.findAllPermissionIds();

        if (!new HashSet<>(permissions).containsAll(dto.permissions())) {
           throw new ResourceNotFoundException("One of the given Permissions was not found.");
        }

        for (UUID permissionId : dto.permissions()) {
            rolesPermissions.add(
                    rolesPermissionsRepository.save(new RolesPermissions(UUID.randomUUID(), createdRole.getId(), permissionId))
            );
        }
        return rolesPermissions;
    }

    @Transactional
    @Override
    public List<RolesPermissions> addPermissionsToRole(UUID roleId, RolesPermissionsUpdate dto) {
        List<RolesPermissions> rolesPermissions = new ArrayList<>();

        Roles roleFromDB = rolesRepository.findById(roleId).orElse(null);

        if (roleFromDB == null) {
            throw new ResourceNotFoundException("Role with id:" + roleId + " was not found.");
        }

        if (roleFromDB.getIsSystemRole()){
            throw new SystemRoleUpdateException("System Roles can not be updated. The role: " + roleFromDB.getName() + " is a system role.");
        }

        List<UUID> permissions = permissionsRepository.findAllPermissionIds();
        List<UUID> rolesPermissionsFromDb = rolesPermissionsRepository.findAllPermissionsOfRoleByRoleId(roleId);

        if (!new HashSet<>(permissions).containsAll(dto.permissions())) {
            throw new ResourceNotFoundException("One of the given Permissions was not found.");
        }

        for (UUID permissionId : dto.permissions()) {
            //controls that duplicates are not created
            if(!rolesPermissionsFromDb.contains(permissionId)) {
                rolesPermissions.add(rolesPermissionsRepository.save(RolesPermissions.builder()
                        .roleId(roleId)
                        .permissionId(permissionId)
                        .build()));
            }
        }

        return rolesPermissions;
    }

    @Transactional
    @Override
    public void removePermissionsFromRole(UUID roleId, RolesPermissionsUpdate dto) {
        Roles roleFromDB = rolesRepository.findById(roleId).orElse(null);

        if (roleFromDB == null) {
            throw new ResourceNotFoundException("Role with id:" + roleId + " was not found.");
        }

        if (roleFromDB.getIsSystemRole()){
            throw new SystemRoleUpdateException("System Roles can not be updated. The role: " + roleFromDB.getName() + " is a system role.");
        }

        List<UUID> permissions = permissionsRepository.findAllPermissionIds();

        if (!new HashSet<>(permissions).containsAll(dto.permissions())) {
            throw new ResourceNotFoundException("One of the given Permissions was not found.");
        }

        for (UUID permissionId : dto.permissions()) {
            rolesPermissionsRepository.deleteById(permissionId);
        }
    }

}
