package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.tasks.TaskDTO;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.repository.TaskRepository;
import org.projectmanagement.domain.services.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TasksServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TasksServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
        return taskRepository.save(newTasks);
    }

    @Override
    public List<Tasks> getAllTask(String projectId) {
        if (projectId != null) {
            return taskRepository.getTasksByProjectId(UUID.fromString(projectId));
        }
        return taskRepository.getTasks();
    }


    @Override
    public Tasks getTaskInfo(String taskId) {
        return null;
    }

    @Override
    public boolean archiveTasks(String taskId) {
        Tasks findTasks = taskRepository.findOne(UUID.fromString(taskId));
        if (findTasks == null) {
            //Application exception
            return false;
        }
        findTasks.toBuilder().status(DefaultStatus.ARCHIVED).build();
        taskRepository.save(findTasks);
        return true;
    }
}
