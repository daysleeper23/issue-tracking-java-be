package org.projectmanagement.application.dto.projects;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.domain.entities.Projects;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "workspaceId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toProjectsFromProjectsUpdateDTO(ProjectsUpdate projectsUpdate, @MappingTarget Projects project);


    @AfterMapping
    default void validateDates(@MappingTarget Projects project) {
        if (project.getStartDate() != null && project.getEndDate() != null &&
                project.getStartDate().isAfter(project.getEndDate())) {
            throw new IllegalArgumentException("Start date must not be after end date.");
        }
    }
}
