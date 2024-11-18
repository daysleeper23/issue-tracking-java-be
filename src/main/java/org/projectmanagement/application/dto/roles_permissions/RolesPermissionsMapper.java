package org.projectmanagement.application.dto.roles_permissions;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.application.dto.project_members.ProjectMemberMapper;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public class RolesPermissionsMapper {
    ProjectMemberMapper INSTANCE = Mappers.getMapper(ProjectMemberMapper.class);
}
