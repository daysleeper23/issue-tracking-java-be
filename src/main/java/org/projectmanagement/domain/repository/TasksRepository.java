package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TasksRepository extends JpaRepository<Tasks,UUID> {

    @Query("SELECT t FROM Tasks t WHERE t.projectId = :projectId")
    List<Tasks> getTasksByProjectId(UUID projectId);

    @Query("SELECT t FROM Tasks t")
    List<Tasks> getTasks();

    @Query("SELECT t FROM Tasks t WHERE t.id = :taskId")
    Tasks findTaskById(UUID taskId);

}
