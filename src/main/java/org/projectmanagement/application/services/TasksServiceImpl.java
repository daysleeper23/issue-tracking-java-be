package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.tasks.*;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.repository.TasksRepository;
import org.projectmanagement.domain.repository.TaskSubscribersRepository;
import org.projectmanagement.domain.services.TasksService;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.projectmanagement.application.exceptions.AppMessage.TASK_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;
    private final TaskSubscribersRepository subscribersRepository;

    @Override
    public TasksInfo addTask(TasksCreate newTask) {
        Tasks newTasks = TasksMapper.mapper.createDtoToEntity(newTask);
        // Todo: user who created this tasks also is a subscriber and order should be after task is created
        //subscriberRepository.save(new TaskSubscribers(taskId, userId));
        return TasksMapper.mapper.entityToInfoDto(tasksRepository.save(newTasks), Collections.emptyList());
    }

    @Override
    public TasksInfo updateTask(String taskId, TasksUpdate taskDTO){
        Tasks existed = tasksRepository.findById(UUID.fromString(taskId));
        if (existed == null){
            throw new ApplicationException(HttpStatus.NOT_FOUND,TASK_NOT_FOUND);
        }
        if (!isChanged(taskDTO,existed)){
            throw new ApplicationException(AppMessage.NO_CHANGE);
        }

        TasksMapper.mapper.updateDtoToEntity(taskDTO,existed);
        return TasksMapper.mapper.entityToInfoDto(
                tasksRepository.save(existed),
                subscribersRepository.getSubscriberByTaskId(existed.getId())
        );
    }

    @Override
    public List<TasksCompact> getAllTask(String projectId, String assigneeId) {
        if (projectId != null) {
            return TasksMapper.mapper.entitiesToCompactDtoList(tasksRepository.findByProjectId(UUID.fromString(projectId)));
        }
        //Todo:only get all tasks from projects that user has access to (custom query planed)
        return TasksMapper.mapper.entitiesToCompactDtoList(tasksRepository.getAllTasks());
    }

    @Override
    public TasksInfo getTaskInfo(String taskId) {
        Tasks info = tasksRepository.findById(UUID.fromString(taskId));
        if (info == null) {
            throw new ApplicationException(TASK_NOT_FOUND);
        }
        List<TaskSubscribers> subscribers = subscribersRepository.getSubscriberByTaskId(info.getId());
        //Move to dto
        return TasksMapper.mapper.entityToInfoDto(info, subscribers);
    }

    @Override
    public boolean archiveTasks(String taskId) {
        Tasks findTasks = tasksRepository.findById(UUID.fromString(taskId));
        if (findTasks == null) {
            throw new ApplicationException(TASK_NOT_FOUND);
        }
        tasksRepository.save(findTasks.toBuilder().status(DefaultStatus.ARCHIVED).build());
        return true;
    }
    //Todo: move to util & optimize
    private boolean isChanged(TasksUpdate updates, Tasks target){
        boolean changed = false;
        for (var method : updates.getClass().getMethods()){
            if (method.getName().startsWith("get") && !method.getName().contains("Class")){
                try {
                    //Method name has to be matched or else will fail
                    Object value = method.invoke(updates);
                    Object targetValue = Stream.of(target.getClass().getMethods())
                            .filter(m -> m.getName().equals(method.getName()))
                            .findFirst()
                            .orElseThrow()
                            .invoke(target);
                    if (value != null && !value.equals(targetValue)){
                        changed = true;
                        break;
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,AppMessage.INTERNAL_ERROR);
                }
            }
        }
        return changed;
    }
}
