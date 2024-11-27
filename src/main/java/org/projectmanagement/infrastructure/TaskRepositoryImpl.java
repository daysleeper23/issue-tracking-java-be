package org.projectmanagement.infrastructure;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.repository.TasksRepository;
import org.projectmanagement.domain.repository.jpa.TasksJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TaskRepositoryImpl implements TasksRepository {

    //Replace with actual implementation for jpa
    private final TasksJpaRepository jpaRepository;

    @Override
    public List<Tasks> findByProjectId(UUID projectId) {
        return jpaRepository.findByProjectId(projectId);
    }

    public Tasks save(Tasks task) {
        //Replace with actual implementation for jpa
        return jpaRepository.save(task);
    }

    @Override
    public Tasks findById(UUID id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Tasks> findAllTasksUserAssociated(UUID userId) {
        return jpaRepository.findAllTasksUserAssociated(userId);
    }

    @Override
    public List<Tasks> saveAll(List<Tasks> tasks) {
        return jpaRepository.saveAll(tasks);
    }

}
