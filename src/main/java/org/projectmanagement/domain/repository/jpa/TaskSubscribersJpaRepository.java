package org.projectmanagement.domain.repository.jpa;


import org.projectmanagement.domain.entities.TaskSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskSubscribersJpaRepository extends JpaRepository<TaskSubscribers, UUID> {

    List<TaskSubscribers> findByTaskId(UUID taskId);

    @Query(value = "SELECT * FROM task_subscribers WHERE task_id =:taskId and user_id =:userId and is_del = false",
            nativeQuery = true)
    TaskSubscribers findByTaskIdAndUserId(UUID taskId, UUID userId);

    @Modifying
    @Query(value = "Update task_subscribers SET is_del = true WHERE task_id =:taskId and user_id =:userId",
            nativeQuery = true)
    int deleteByTaskIdAndUserId(@Param("taskId")UUID taskId,@Param("userId") UUID userId);
}
