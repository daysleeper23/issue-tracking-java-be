package org.projectmanagement.application.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesMapper;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesRead;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
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

    public WorkspacesMembersRolesRead createWorkspacesMembersRoles(WorkspacesMembersRolesCreate wmrc) {
        if (wmrRepository.findByUserIdAndWorkspaceId(wmrc.getUserId(), wmrc.getWorkspaceId()).isPresent()) {
            return null;
        }

        WorkspacesMembersRoles wmr = new WorkspacesMembersRoles(
                UUID.randomUUID(),
                wmrc.getUserId(),
                wmrc.getWorkspaceId(),
                wmrc.getRoleId(),
                Instant.now(),
                Instant.now()
        );
        wmrRepository.save(wmr);
        return WorkspacesMembersRolesMapper.toWorkspacesMembersRolesRead(wmr);
    }

    //get the roles for all members in a workspace
    public List<WorkspacesMembersRolesRead> getWorkspacesMembersRoles(UUID workspaceId) {
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

    //delete a role for a user in a workspace == remove the user from the workspace
    public void deleteWorkspacesMembersRoles(@NotNull UUID id) {

        wmrRepository.deleteById(id);
    }
}
