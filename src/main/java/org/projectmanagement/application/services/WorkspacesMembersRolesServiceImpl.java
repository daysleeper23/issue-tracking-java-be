package org.projectmanagement.application.services;

import jakarta.transaction.Transactional;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesMapper;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesRead;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.*;
import org.projectmanagement.domain.services.WorkspacesMembersRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@Service
public class WorkspacesMembersRolesServiceImpl implements WorkspacesMembersRolesService {
    private final WorkspacesMembersRolesRepository wmrRepository;
    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;
    private final WorkspacesRepository workspacesRepository;

    @Autowired
    public WorkspacesMembersRolesServiceImpl(
        WorkspacesMembersRolesRepository workspacesMembersRolesRepository
        , RolesRepository inRolesRepository
        , UsersRepository inUsersRepository
        , WorkspacesRepository inWorkspacesRepository
    ) {
        this.wmrRepository = workspacesMembersRolesRepository;
        this.rolesRepository = inRolesRepository;
        this.usersRepository = inUsersRepository;
        this.workspacesRepository = inWorkspacesRepository;
    }

    //create a role for a user in a workspace == add a user to a workspace
    public WorkspacesMembersRolesRead createMembersRolesForWorkspace(
            UUID workspaceId,
            WorkspacesMembersRolesCreate wmrc) {
        //check if the role id is valid
        Optional<Roles> roleInfo = rolesRepository.findById(wmrc.roleId());
        if (roleInfo.isEmpty()) {
            throw new ApplicationException(AppMessage.ROLE_NOT_FOUND);
        }

        //TODO: should have level-based role for better structure
        //check if the role is member or workspace manager
        if (roleInfo.get().getName().equals("Super Admin") || roleInfo.get().getName().equals("Company Managers")) {
            throw new ApplicationException(AppMessage.WMR_INVALID_ROLE);
        }

        //check if the user exists
        if (usersRepository.findById(wmrc.userId()).isEmpty()) {
            throw new ApplicationException(AppMessage.USER_NOT_FOUND);
        }

        //check if the workspace exists
        if (workspacesRepository.findById(workspaceId).isEmpty()) {
            throw new ApplicationException(AppMessage.WORKSPACE_NOT_FOUND);
        }

        //check if a role for the user in the workspace exists already, if it does return null
        if (wmrRepository.findByUserIdAndWorkspaceId(wmrc.userId(), workspaceId).isPresent()) {
            throw new ApplicationException(AppMessage.WRM_USER_ALREADY_IN_WORKSPACE);
        }

        //create a new role for the user in the workspace
        WorkspacesMembersRoles wmr = wmrRepository.save(
                WorkspacesMembersRoles.builder().userId(wmrc.userId())
                        .workspaceId(workspaceId)
                        .roleId(wmrc.roleId())
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );
        return WorkspacesMembersRolesMapper.toWorkspacesMembersRolesRead(wmr);
    }

    //get the roles for all members in a workspace
    public List<WorkspacesMembersRolesRead> getMembersRolesForWorkspace(UUID workspaceId) {
        List<WorkspacesMembersRoles> wmrl = wmrRepository.findAllByWorkspaceId(workspaceId);
        return wmrl.stream().map(WorkspacesMembersRolesMapper::toWorkspacesMembersRolesRead).toList();
    }

    //get the role of a user in a workspace using user id and workspace id
    public WorkspacesMembersRolesRead getWorkspacesMembersRolesForUser(
            UUID userId,
            UUID workspaceId)
    {
        WorkspacesMembersRoles wmr = wmrRepository.findByUserIdAndWorkspaceId(userId, workspaceId).orElse(null);
        if (wmr == null) {
            return null;
        }
        return WorkspacesMembersRolesMapper.toWorkspacesMembersRolesRead(wmr);
    }

    //update role for a user in a workspace
    @Transactional
    public WorkspacesMembersRolesRead updateWorkspacesMembersRoles(
            UUID id,
            UUID workspaceId,
            WorkspacesMembersRolesCreate newRole)
    {
        //check if WMR id exists
        Optional<WorkspacesMembersRoles> wmr = wmrRepository.findById(id);
        if (wmr.isEmpty()) {
            throw new ApplicationException(AppMessage.WMR_ROLE_NOT_FOUND);
        }

        //check if the workspace id and user id of the new role are the same as the existing role
        if (!wmr.get().getWorkspaceId().equals(workspaceId)) {
            throw new ApplicationException(AppMessage.WMR_INVALID_WORKSPACE);
        }

        System.out.println("Existing user id: " + wmr.get().getUserId() + " - Updating user id: " + newRole.userId());
        if (!wmr.get().getUserId().equals(newRole.userId())) {
            throw new ApplicationException(AppMessage.WMR_INVALID_USER);
        }

        //check if the new role id is valid
        Optional<Roles> roleInfo = rolesRepository.findById(newRole.roleId());
        if (roleInfo.isEmpty()) {
            throw new ApplicationException(AppMessage.ROLE_NOT_FOUND);
        }

        //TODO: should have level-based role for better structure
        //check if the new role is member or workspace manager, if not throw an error
        if (roleInfo.get().getName().equals("Super Admin") || roleInfo.get().getName().equals("Company Managers")) {
            throw new ApplicationException(AppMessage.WMR_INVALID_ROLE);
        }

        //update the role using the new roleId from newRole
        Optional<WorkspacesMembersRoles> newWmr = wmrRepository.updateWorkspacesMembersRoles(id, newRole.roleId());
        if (newWmr.isEmpty()) {
            throw new ApplicationException(AppMessage.WMR_UPDATE_ERROR);
        }
        return WorkspacesMembersRolesMapper.toWorkspacesMembersRolesRead(newWmr.get());
    }

    //delete a role for a user in a workspace == remove the user from the workspace
    @Transactional
    public void deleteWorkspacesMembersRoles(UUID id, UUID workspaceId) {
        //check if the member is in the workspace with a role, if not return null
        Optional<WorkspacesMembersRoles> wmr = wmrRepository.findById(id);
        if (wmr.isEmpty()) {
            throw new ApplicationException(AppMessage.WMR_ROLE_NOT_FOUND);
        }

        //check if the workspace id of the role is the same as the workspace id in the path
        if (!wmr.get().getWorkspaceId().equals(workspaceId)) {
            throw new ApplicationException(AppMessage.WMR_INVALID_WORKSPACE);
        }

        //delete the role
        wmrRepository.deleteById(id);
    }

    @Override
    public List<WorkspacesMembersRoles> getAllWorkspacesForUser(UUID userId) {
        return wmrRepository.findByUserId(userId);
    }
}
