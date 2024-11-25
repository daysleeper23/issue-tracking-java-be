package org.projectmanagement.application.services;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesMapper;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesRead;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepository;
import org.projectmanagement.domain.services.RolesService;
import org.projectmanagement.domain.services.WorkspacesMembersRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Validated
@Service
public class WorkspacesMembersRolesServiceImpl implements WorkspacesMembersRolesService {
    private final WorkspacesMembersRolesRepository wmrRepository;
    private final RolesService rolesService;

    @Autowired
    public WorkspacesMembersRolesServiceImpl(
            WorkspacesMembersRolesRepository workspacesMembersRolesRepository,
            RolesService inRolesService) {
        this.wmrRepository = workspacesMembersRolesRepository;
        this.rolesService = inRolesService;
    }

    //create a role for a user in a workspace == add a user to a workspace
    public WorkspacesMembersRolesRead createMembersRolesForWorkspace(
            UUID workspaceId,
            WorkspacesMembersRolesCreate wmrc) {
        //MISSING: check if the user id and workspace id and role id are valid
        if (rolesService.findById(wmrc.getRoleId()).isEmpty()) {
            return null;
        }

        //check if a role for the user in the workspace exists already, if it does return null
        if (wmrRepository.findByUserIdAndWorkspaceId(wmrc.getUserId(), workspaceId).isPresent()) {
            return null;
        }

        WorkspacesMembersRoles wmr = wmrRepository.save(
                WorkspacesMembersRoles.builder().userId(wmrc.getUserId())
                        .workspaceId(workspaceId)
                        .roleId(wmrc.getRoleId())
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
        //check if the role exists already, if not return null
        WorkspacesMembersRoles wmr = wmrRepository.findById(id).orElse(null);
        if (wmr == null) {
            return null;
        }

        //check if the workspace id and user id of the new role are the same as the existing role
        //if not return null
        if (wmr.getWorkspaceId() != workspaceId || wmr.getUserId() != newRole.getUserId()) {
            return null;
        }

        //update the role using the new roleId from newRole
        wmr = wmrRepository.updateWorkspacesMembersRoles(id, newRole.getRoleId());
        return WorkspacesMembersRolesMapper.toWorkspacesMembersRolesRead(wmr);
    }

    //delete a role for a user in a workspace == remove the user from the workspace
    public void deleteWorkspacesMembersRoles(UUID id) {

        wmrRepository.deleteById(id);
    }
}
