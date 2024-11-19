package org.projectmanagement.domain.repository.jpa;

import org.projectmanagement.domain.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TasksJpaRepository extends JpaRepository<Tasks, UUID> {
    @Query(value ="SELECT t FROM tasks t WHERE t.project_id = :projectId",
            nativeQuery = true
    )
    List<Tasks> findByProjectId(UUID projectId);
}
