package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.TaskSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskSubscribersRepository extends JpaRepository<TaskSubscribers, UUID> {

    @Query("SELECT ts FROM task_subscribers ts WHERE ts.taskId = :taskId")
    List<TaskSubscribers> getSubscriberByTaskId(UUID taskId);

    @Query("SELECT ts FROM task_subscribers ts WHERE ts.taskId = :taskId AND ts.userId = :userId")
    TaskSubscribers getSubscriberByTaskIdAndUserId(UUID taskId, UUID userId);

    @Query("DELETE FROM task_subscribers ts WHERE ts.taskId = :taskId AND ts.userId = :userId")
    boolean deleteByTaskIdAndUserId(UUID taskId, UUID userId);
}
