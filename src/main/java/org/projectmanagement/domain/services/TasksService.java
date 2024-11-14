package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.tasks.TaskDTO;
import org.projectmanagement.application.dto.tasks.TaskInfo;
import org.projectmanagement.domain.entities.Tasks;

import java.util.List;


public interface TasksService {

    Tasks addTask(TaskDTO taskDTO);

    Tasks updateTask(String taskId, TaskDTO taskDTO);

    List<Tasks> getAllTask(String projectId);

    TaskInfo getTaskInfo(String taskId);

    boolean archiveTasks(String taskId);

}
