package org.projectmanagement.application.dto.tasks;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TasksMapper {

    TasksMapper mapper = Mappers.getMapper( TasksMapper.class );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tasks.status", source = "taskUpdate.status",qualifiedByName = "statusToEnum")
    void updateDtoToEntity(TasksUpdate taskUpdate, @MappingTarget Tasks tasks);

    Tasks createDtoToEntity(TasksCreate taskCreate);

    @Mapping(target = "subscribers", source = "subscribers")
    TaskInfo entityToInfoDto(Tasks task, List<TaskSubscribers> subscribers);

    List<TasksCompact> entitiesToCompactDtoList(List<Tasks> tasks);

    @Named("statusToEnum")
    default DefaultStatus toEnum(String status) {
        return DefaultStatus.valueOf(status);
    }

    @Named("enumToString")
    default String toStatus(DefaultStatus status) {
        return status.name();
    }

}
