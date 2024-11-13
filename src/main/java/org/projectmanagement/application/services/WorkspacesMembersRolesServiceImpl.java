package org.projectmanagement.application.services;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesMapper;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesRead;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@Service
public class WorkspacesMembersRolesServiceImpl {
    private final WorkspacesMembersRolesRepository wmrRepository;

    @Autowired
    public WorkspacesMembersRolesServiceImpl(WorkspacesMembersRolesRepository workspacesMembersRolesRepository) {
        this.wmrRepository = workspacesMembersRolesRepository;
    }

    public WorkspacesMembersRoles createWorkspacesMembersRoles(WorkspacesMembersRoles wmr) {
        if (wmrRepository.findByUserIdAndWorkspaceId(wmr.getUserId(), wmr.getWorkspaceId()).isPresent()) {
            return null;
        }
        return null;
    }

    //get the roles for all members in a workspace
    public List<WorkspacesMembersRolesRead> getWorkspacesMembersRoles(@Valid UUID workspaceId) {
        List<WorkspacesMembersRoles> wmrl = wmrRepository.findAllByWorkspaceId(workspaceId);
        return wmrl.stream().map(WorkspacesMembersRolesMapper::toWorkspacesMembersRolesRead).toList();
    }

    //get the role of a user in a workspace using user id and workspace id
    public Optional<WorkspacesMembersRolesRead> getWorkspacesMembersRolesForUser(
            @Valid UUID userId,
            @Valid UUID workspaceId)
    {
        WorkspacesMembersRoles wmr = wmrRepository.findByUserIdAndWorkspaceId(userId, workspaceId).orElse(null);
        if (wmr == null) {
            return Optional.empty();
        }
        return Optional.of(WorkspacesMembersRolesMapper.toWorkspacesMembersRolesRead(wmr));
    }

    //update role for a user in a workspace
    public WorkspacesMembersRolesRead updateWorkspacesMembersRoles(
            @Valid UUID id,
            @Valid WorkspacesMembersRolesCreate newRole)
    {

        WorkspacesMembersRoles wmr = wmrRepository.findById(id).orElse(null);
        if (wmr == null) {
            return null;
        }

        if (wmr.getWorkspaceId() != newRole.getWorkspaceId() || wmr.getUserId() != newRole.getUserId()) {
            return null;
        }

        wmr = wmrRepository.updateWorkspacesMembersRoles(id, newRole.getRoleId());
        return WorkspacesMembersRolesMapper.toWorkspacesMembersRolesRead(wmr);
    }
}
