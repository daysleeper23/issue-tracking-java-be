package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.tasks.TasksCompact;
import org.projectmanagement.application.dto.tasks.TasksCreate;
import org.projectmanagement.application.dto.tasks.TasksInfo;
import org.projectmanagement.application.dto.tasks.TasksUpdate;

import java.util.List;


public interface TasksService {

    TasksInfo addTask(TasksCreate dto);

    TasksInfo updateTask(String taskId, TasksUpdate dto);

    List<TasksCompact> getAllTaskByUser();

    List<TasksCompact> getAllTaskInProject(String projectId);

    List<TasksInfo> getAllTaskInWorkspace(String workspaceId);

    TasksInfo getTaskInfo(String taskId);

    boolean archiveTasks(String taskId);

}
