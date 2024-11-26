package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.repository.jpa.TasksJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TasksDataFactory {
    @Autowired
    private TasksJpaRepository tasksRepoJpa;

    public UUID createTask(String taskName, UUID projectId, UUID userId) {
        return tasksRepoJpa.save(org.projectmanagement.domain.entities.Tasks.builder()
                .name(taskName)
                .description("A test task")
                .projectId(projectId)
                .assigneeId(userId)
                .build()).getId();
    }

    public void deleteAll() {
        tasksRepoJpa.deleteAll();
    }
}
