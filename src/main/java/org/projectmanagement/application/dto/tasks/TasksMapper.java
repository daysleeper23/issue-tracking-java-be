package org.projectmanagement.application.dto.tasks;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.entities.Tasks;

import java.util.List;

@Mapper
public interface TasksMapper {

    TasksMapper mapper = Mappers.getMapper( TasksMapper.class );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tasks createDtoToEntity(TasksCreate taskCreate);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDtoToEntity(TasksUpdate taskUpdate, @MappingTarget Tasks tasks);

    @Mapping(target = "subscribers", source = "subscribers")
    TasksInfo entityToInfoDto(Tasks task, List<TaskSubscribers> subscribers);

    List<TasksCompact> entitiesToCompactDtoList(List<Tasks> tasks);

}
