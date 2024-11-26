package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.repository.jpa.TaskSubscribersJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaskSubscribersDataFactory {

    @Autowired
    private TaskSubscribersJpaRepository taskSubscribersRepoJpa;

    public void addSubscriberToTask(UUID taskId, UUID userId) {
        taskSubscribersRepoJpa.save( new TaskSubscribers(taskId,userId));
    }

    public void deleteAll() {
        taskSubscribersRepoJpa.deleteAll();
    }

}
