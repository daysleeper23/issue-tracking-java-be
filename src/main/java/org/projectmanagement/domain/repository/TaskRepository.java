package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Tasks;

import java.util.List;
import java.util.UUID;

public interface TaskRepository {
    Tasks save(Tasks task);

    List<Tasks> getTasksByProjectId(UUID projectId);

    List<Tasks> getTasks();

    Tasks findOne(UUID taskId);

}
