package org.projectmanagement.infrastructure;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.repository.TasksRepository;
import org.projectmanagement.domain.repository.jpa.TasksJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Repository
public class TaskRepositoryImpl implements TasksRepository {

    //Replace with actual implementation for jpa
    private TasksJpaRepository jpaRepository;

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
        return null;
    }

    @Override
    public List<Tasks> getAllTasks() {
        return jpaRepository.findAll();
    }

    public List<Tasks> getTasksByProjectId(UUID projectId) {
        return jpaRepository.findByProjectId(projectId);
    }

    public Tasks getTask(UUID taskId) {
        return null;
    }
}
