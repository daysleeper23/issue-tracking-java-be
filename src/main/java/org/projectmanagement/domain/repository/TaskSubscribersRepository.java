package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.TaskSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskSubscribersRepository {

    TaskSubscribers save(TaskSubscribers taskSubscribers);

    List<TaskSubscribers> getSubscriberByTaskId(UUID taskId);


    TaskSubscribers getSubscriberByTaskIdAndUserId(UUID taskId, UUID userId);

    boolean deleteByTaskIdAndUserId(UUID taskId, UUID userId);
}
