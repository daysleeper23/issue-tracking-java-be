package org.projectmanagement.application.dto.project_members;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.application.dto.projects.ProjectMapper;
import org.projectmanagement.domain.entities.ProjectMembers;


@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface  ProjectMemberMapper {

    ProjectMemberMapper INSTANCE = Mappers.getMapper(ProjectMemberMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toProjectMemberFromProjectMemberDTO(ProjectMemberUpdateDTO dto, @MappingTarget ProjectMembers projectMember);

}
