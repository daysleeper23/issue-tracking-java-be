package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.tasks.TaskDTO;
import org.projectmanagement.application.exception.AppMessage;
import org.projectmanagement.application.exception.ApplicationException;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.repository.TasksRepository;
import org.projectmanagement.domain.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.projectmanagement.application.exception.AppMessage.TASK_NOT_FOUND;

@Service
public class TasksServiceImpl implements TaskService {

    private final TasksRepository tasksRepository;

    public TasksServiceImpl(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Tasks addTask(TaskDTO newTask) {
        Tasks newTasks = Tasks.builder()
                .id(UUID.randomUUID())
                .name(newTask.name())
                .description(newTask.description())
                .projectId(UUID.fromString(newTask.projectId()))
                .priority(newTask.priority())
                .startedAt(newTask.startedAt())
                .status(DefaultStatus.TODO)
                .endedAt(newTask.endedAt()).build();
        if (newTask.assigneeId() != null && !newTask.assigneeId().isEmpty()) {
            newTasks.toBuilder().assigneeId(UUID.fromString(newTask.assigneeId())).build();
        }
        return tasksRepository.save(newTasks);
    }

    @Override
    public Tasks updateTask(String taskId, TaskDTO taskDTO){
        Tasks existed = tasksRepository.findOne(UUID.fromString(taskId));
        if (existed == null){
            throw new ApplicationException(HttpStatus.NOT_FOUND,TASK_NOT_FOUND);
        }
        if (!isChanged(taskDTO,existed)){
            throw new ApplicationException(AppMessage.TASK_NO_CHANGE);
        }
        existed.toBuilder()
                .name(taskDTO.name())
                .description(taskDTO.description())
                .priority(taskDTO.priority())
                .stringToStatus(taskDTO.status())
                .assigneeId(UUID.fromString(taskDTO.assigneeId()))
                .startedAt(taskDTO.startedAt())
                .endedAt(taskDTO.endedAt())
                .updatedAt(Instant.now())
                .build();
        return tasksRepository.save(existed);
    }

    @Override
    public List<Tasks> getAllTask(String projectId) {
        if (projectId != null) {
            return tasksRepository.getTasksByProjectId(UUID.fromString(projectId));
        }
        return tasksRepository.getTasks();
    }

    @Override
    public boolean archiveTasks(String taskId) {
        Tasks findTasks = tasksRepository.findOne(UUID.fromString(taskId));
        if (findTasks == null) {
            //Application exception
            return false;
        }
        findTasks.toBuilder().status(DefaultStatus.ARCHIVED).build();
        tasksRepository.save(findTasks);
        return true;
    }

    private boolean isChanged(TaskDTO updates,Tasks target){
        return !(updates.name().equals(target.getName()) &&
                target.getStatus().toString().equals(updates.status()) &&
                updates.description().equals(target.getDescription()) &&
                updates.priority() != target.getPriority() &&
                updates.startedAt().equals(target.getStartedAt()) &&
                updates.endedAt().equals(target.getEndedAt()) &&
                updates.assigneeId().equals(target.getAssigneeId().toString()));
    }
}
