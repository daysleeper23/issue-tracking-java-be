package org.projectmanagement.domain.repository.jpa;

import org.projectmanagement.domain.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TasksJpaRepository extends JpaRepository<Tasks, UUID> {
    @Query(value ="SELECT * FROM tasks t WHERE t.project_id = :projectId",
            nativeQuery = true
    )
    List<Tasks> findByProjectId(@Param("projectId")UUID projectId);

    @Query(value ="SELECT t.id, " +
            "t.name," +
            "t.description," +
            "t.priority," +
            "t.status," +
            "t.started_at," +
            "t.ended_at," +
            "t.assignee_id," +
            "t.project_id," +
            "t.created_at," +
            "t.updated_at " +
            "FROM tasks t " +
            "LEFT JOIN task_subscribers ts ON ts.task_id = t.id " +
            "WHERE t.assignee_id = :userId OR ts.user_id = :userId",
            nativeQuery = true
    )
    List<Tasks> findAllTasksUserAssociated(@Param("userId")UUID userId);
}
