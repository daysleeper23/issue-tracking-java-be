package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.tasks.TaskDTO;
import org.projectmanagement.domain.entities.Tasks;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskService {

    Tasks addTask(TaskDTO taskDTO);

    List<Tasks> getAllTask(String projectId);

    Tasks getTaskInfo(String taskId);

    boolean archiveTasks(String taskId);
}
