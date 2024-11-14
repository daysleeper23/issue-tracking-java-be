package org.projectmanagement.application.dto.workspaces;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersMapper;
import org.projectmanagement.application.dto.users.UsersUpdate;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.entities.Workspaces;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkspacesMapper {

    WorkspacesMapper INSTANCE = Mappers.getMapper(WorkspacesMapper.class);

    public static WorkspacesRead toWorkspacesRead(Workspaces workspaces) {
        return new WorkspacesRead(
                workspaces.getId(),
                workspaces.getName(),
                workspaces.getDescription(),
                workspaces.getCompanyId(),
                workspaces.getCreatedAt(),
                workspaces.getUpdatedAt());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toWorkspacesFromWorkspacesCreate(WorkspacesCreate wc, @MappingTarget Workspaces w);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toWorkspacesFromWorkspacesUpdate(WorkspacesUpdate wu, @MappingTarget Workspaces w);
}
