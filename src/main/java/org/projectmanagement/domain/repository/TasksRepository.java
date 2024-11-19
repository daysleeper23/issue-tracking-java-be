package org.projectmanagement.domain.repository;


import org.projectmanagement.domain.entities.Tasks;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface TasksRepository {

    Tasks save(Tasks task);

    List<Tasks> findByProjectId(UUID projectId);

    Tasks findById(UUID id);

    List<Tasks> getAllTasks();
}
