package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.tasks.TasksCompact;
import org.projectmanagement.application.dto.tasks.TasksCreate;
import org.projectmanagement.application.dto.tasks.TasksUpdate;
import org.projectmanagement.application.dto.tasks.TaskInfo;
import org.projectmanagement.domain.entities.Tasks;

import java.util.List;


public interface TasksService {

    Tasks addTask(TasksCreate dto);

    Tasks updateTask(String taskId, TasksUpdate dto);

    List<TasksCompact> getAllTask(String projectId);

    TaskInfo getTaskInfo(String taskId);

    boolean archiveTasks(String taskId);

}
