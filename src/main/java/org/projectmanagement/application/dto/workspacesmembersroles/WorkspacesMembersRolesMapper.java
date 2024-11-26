package org.projectmanagement.application.dto.workspacesmembersroles;

import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;

@Mapper(componentModel = "spring")
public interface WorkspacesMembersRolesMapper {
    WorkspacesMembersRolesMapper INSTANCE = Mappers.getMapper(WorkspacesMembersRolesMapper.class);

    public static WorkspacesMembersRolesRead toWorkspacesMembersRolesRead(WorkspacesMembersRoles wmr) {
        return new WorkspacesMembersRolesRead(wmr.getId(), wmr.getUserId(), wmr.getWorkspaceId(), wmr.getRoleId(), wmr.getUpdatedAt());
    }
}
