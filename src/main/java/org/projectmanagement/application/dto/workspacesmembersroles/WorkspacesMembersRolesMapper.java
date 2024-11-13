package org.projectmanagement.application.dto.workspacesmembersroles;

import org.mapstruct.factory.Mappers;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersRead;
import org.projectmanagement.domain.entities.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;

@Mapper(componentModel = "spring")
public interface WorkspacesMembersRolesMapper {
    WorkspacesMembersRolesMapper INSTANCE = Mappers.getMapper(WorkspacesMembersRolesMapper.class);

    public static WorkspacesMembersRolesRead toWorkspacesMembersRolesRead(WorkspacesMembersRoles wmr) {
        return new WorkspacesMembersRolesRead(wmr.getId(), wmr.getWorkspaceId(), wmr.getUserId(), wmr.getRoleId(), wmr.getUpdatedAt());
    }

    void toUsersFromUsersCreate(UsersCreate userCreate, @MappingTarget Users user);
}
