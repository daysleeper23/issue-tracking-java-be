package org.projectmanagement.domain.repository.jpa;


import org.projectmanagement.domain.entities.TaskSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskSubscribersJpaRepository extends JpaRepository<TaskSubscribers, UUID> {

    List<TaskSubscribers> findByTaskId(UUID taskId);

    TaskSubscribers findByTaskIdAndUserId(UUID taskId, UUID userId);

    @Modifying
    @Query(value = "Update task_subscribers ts SET ts.is_del = 1 WHERE ts.task_id =:taskId and ts.user_id =:userId",
            nativeQuery = true)
    boolean deleteByTaskIdAndUserId(UUID taskId, UUID userId);
}
