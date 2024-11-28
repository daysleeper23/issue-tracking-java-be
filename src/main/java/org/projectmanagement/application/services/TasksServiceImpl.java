package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.tasks.*;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.repository.*;
import org.projectmanagement.domain.services.TasksService;
import org.projectmanagement.presentation.config.SecurityUtils;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.projectmanagement.application.exceptions.AppMessage.TASK_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;
    private final TaskSubscribersRepository subscribersRepository;
    private final ProjectMembersRepository projectMembersRepository;
    private final ProjectsRepository projectsRepository;
    private final UsersRepository usersRepository;

    @Override
    public TasksInfo addTask(TasksCreate newTask) {
        //Todo: check if user is in project, should retrieve userId from context holder rather than has to query db
        UserDetails user = SecurityUtils.getCurrentUser();
        Users users = usersRepository.findOneByEmail(user.getUsername())
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND));
        if (projectMembersRepository
                .findByProjectIdAndUserId(UUID.fromString(newTask.projectId()),
                        users.getId()).isEmpty()){
            throw new ApplicationException(HttpStatus.FORBIDDEN, AppMessage.USER_NOT_IN_PROJECT);
        }
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
    public List<TasksCompact> getAllTaskInProject(String projectId) {
        UserDetails user = SecurityUtils.getCurrentUser();
        Users users =usersRepository.findOneByEmail(user.getUsername())
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND));
        Optional<Projects> project = projectsRepository.findOneById(UUID.fromString(projectId));
        if (project.isEmpty()) {
            throw new ApplicationException(AppMessage.PROJECT_NOT_FOUND);
        }
        return TasksMapper.mapper.entitiesToCompactDtoList(tasksRepository.findByProjectId(UUID.fromString(projectId)));
    }

    @Override
    public List<TasksCompact> getAllTaskByUser() {
        UserDetails user = SecurityUtils.getCurrentUser();
        Users users =usersRepository.findOneByEmail(user.getUsername())
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND));
        return TasksMapper.mapper.entitiesToCompactDtoList(tasksRepository.findAllTasksUserAssociated(users.getId()));
    }

    @Override
    public TasksInfo getTaskInfo(String taskId) {
        UserDetails user = SecurityUtils.getCurrentUser();
        Users users =usersRepository.findOneByEmail(user.getUsername())
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND));
        Tasks info = tasksRepository.findById(UUID.fromString(taskId));
        if (info == null) {
            throw new ApplicationException(TASK_NOT_FOUND);
        }
        if (projectMembersRepository
                .findByProjectIdAndUserId(info.getProjectId(),
                        users.getId()).isEmpty()){
            throw new ApplicationException(HttpStatus.FORBIDDEN, AppMessage.USER_NOT_IN_PROJECT);
        }
        List<TaskSubscribers> subscribers = subscribersRepository.getSubscriberByTaskId(info.getId());
        return TasksMapper.mapper.entityToInfoDto(info, subscribers);
    }

    @Override
    public boolean archiveTasks(String taskId) {
        UserDetails user = SecurityUtils.getCurrentUser();
        Users users =usersRepository.findOneByEmail(user.getUsername())
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND));
        Tasks info = tasksRepository.findById(UUID.fromString(taskId));
        if (info == null) {
            throw new ApplicationException(TASK_NOT_FOUND);
        }
        if (projectMembersRepository
                .findByProjectIdAndUserId(info.getProjectId(),
                        users.getId()).isEmpty()){
            throw new ApplicationException(HttpStatus.FORBIDDEN, AppMessage.USER_NOT_IN_PROJECT);
        }
        tasksRepository.save(info.toBuilder().status(DefaultStatus.ARCHIVED).build());
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
